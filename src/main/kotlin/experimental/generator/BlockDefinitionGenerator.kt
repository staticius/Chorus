package org.chorus_oss.chorus.experimental.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.block.BlockProperties
import org.chorus_oss.chorus.experimental.block.BlockDefinition
import java.io.File
import kotlin.reflect.KClass

object BlockDefinitionGenerator {
    val properties = mutableMapOf<String, BlockProperties>()
    val components = mutableMapOf<String, MutableMap<String, String>>()

    data class AutogenData(
        val identifier: String,
        val properties: BlockProperties,
        val components: MutableMap<String, String>
    )

    fun populateFromClasses(classes: Map<String, KClass<out Block>>) {

    }

    fun populateFromInstances(instances: List<Block>) {
        instances.forEach {
            val components = this.components.computeIfAbsent(it.properties.identifier) { mutableMapOf() }
            if (!it.isSolid) components["SolidComponent"] = "solid = false"
            if (it.isTransparent) components["TransparentComponent"] = "transparent = true"
        }
    }

    fun populateFromProperties(properties: Map<String, BlockProperties>) {
        this.properties += properties
    }

    fun buildDefinitions() {
        val autogenData = mutableListOf<AutogenData>()

        this.properties.forEach {
            val components = this.components[it.key] ?: mutableMapOf()

            autogenData += AutogenData(it.key, it.value, components)
        }

        val objects = autogenData.map { generateDefinition(it) }
        val objectsString = objects.joinToString(", ") { it }

        val listOfObjects = PropertySpec.builder("definitions", List::class.parameterizedBy(BlockDefinition::class))
            .initializer("listOf($objectsString)")
            .build()

        val fileBuilder = FileSpec.builder("org.chorus_oss.chorus.experimental.block", "Definitions")
            .addProperty(listOfObjects)
            .apply {
                for (obj in objects) {
                    addImport("org.chorus_oss.chorus.experimental.block.definitions", obj)
                }
            }

        val path = File(Server.instance.dataPath + "generated/")
        if (!path.exists()) {
            path.mkdirs()
        }

        val fileSpec = fileBuilder.build()
        fileSpec.writeTo(path)
    }

    fun generateDefinition(genData: AutogenData): String {
        val properties = genData.properties
        val components = genData.components

        val identifier = properties.identifier
        val trimmedIdentifier = identifier.substringAfter(':')
        val pascalIdentifier = trimmedIdentifier.split('_').joinToString("") { it.replaceFirstChar(Char::uppercase)  }

        val states = properties.getPropertyTypeSet().toList().map { it.name.split("_", ":")
            .mapIndexed { index, word ->
                if (index == 0) word else word.replaceFirstChar(Char::uppercase)
            }
            .joinToString("")
        }.sorted()

        val formattedStates = states.joinToString(", ") { "CommonStates.$it"}

        val hasStates = states.isNotEmpty()
        val hasComponents = components.isNotEmpty()

        val formattedComponents = components.map {
            "${it.key}(${it.value})"
        }.joinToString(", ")

        val blockObject = TypeSpec.objectBuilder(pascalIdentifier)
            .superclass(ClassName("org.chorus_oss.chorus.experimental.block", "BlockDefinition"))
            .addSuperclassConstructorParameter("identifier = %S", identifier)
            .apply {
                if (hasStates) addSuperclassConstructorParameter("states = listOf($formattedStates)")
                if (hasComponents) addSuperclassConstructorParameter("components = listOf($formattedComponents)")
            }
            .build()

        val fileBuilder = FileSpec.builder("org.chorus_oss.chorus.experimental.block.definitions", pascalIdentifier)
            .addType(blockObject)

        if (hasStates) {
            fileBuilder.addImport("org.chorus_oss.chorus.experimental.block.state", "CommonStates")
        }

        if (hasComponents) {
            components.keys.forEach {
                fileBuilder.addImport("org.chorus_oss.chorus.experimental.block.components", it)
            }
        }

        val path = File(Server.instance.dataPath + "generated/")
        if (!path.exists()) {
            path.mkdirs()
        }

        val fileSpec = fileBuilder.build()
        fileSpec.writeTo(path)

        return pascalIdentifier
    }
}