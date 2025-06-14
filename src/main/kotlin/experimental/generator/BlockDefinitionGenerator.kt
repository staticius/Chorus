package org.chorus_oss.chorus.experimental.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.block.BlockProperties
import org.chorus_oss.chorus.experimental.block.BlockDefinition
import java.io.File

object BlockDefinitionGenerator {
    fun generateDefinitions(properties: List<BlockProperties>) {
        val objects = properties.map { generateDefinition(it) }
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

    fun generateDefinition(properties: BlockProperties): String {
        val identifier = properties.identifier
        val trimmedIdentifier = identifier.substringAfter(':')
        val pascalIdentifier = trimmedIdentifier.split('_').joinToString("") { it.replaceFirstChar(Char::uppercase)  }

        val componentIDs = properties.getPropertyTypeSet().toList().map { it.name.split("_", ":")
            .mapIndexed { index, word ->
                if (index == 0) word else word.replaceFirstChar(Char::uppercase)
            }
            .joinToString("")
        }.sorted()

        val formattedStates = componentIDs.joinToString(", ") { "CommonStates.$it"}

        val needsImport = componentIDs.isNotEmpty()

        val blockObject = TypeSpec.objectBuilder(pascalIdentifier)
            .superclass(ClassName("org.chorus_oss.chorus.experimental.block", "BlockDefinition"))
            .addSuperclassConstructorParameter("identifier = %S", identifier)
            .addSuperclassConstructorParameter("states = listOf($formattedStates)")
            .build()

        val fileBuilder = FileSpec.builder("org.chorus_oss.chorus.experimental.block.definitions", pascalIdentifier)
            .addType(blockObject)

        if (needsImport) {
            fileBuilder.addImport("org.chorus_oss.chorus.experimental.block.state", "CommonStates")
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