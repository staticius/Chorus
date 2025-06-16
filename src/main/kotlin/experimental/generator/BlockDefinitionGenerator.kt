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
import org.chorus_oss.chorus.experimental.block.components.MoveableComponent
import org.chorus_oss.chorus.registry.BlockRegistry.Companion.CACHE_CONSTRUCTORS
import org.chorus_oss.chorus.registry.BlockRegistry.Companion.PROPERTIES
import org.chorus_oss.chorus.utils.BlockColor
import org.chorus_oss.chorus.utils.Loggable
import java.io.File
import kotlin.reflect.KClass

object BlockDefinitionGenerator : Loggable {
    val properties = mutableMapOf<String, BlockProperties>()
    val components = mutableMapOf<String, MutableList<Pair<String, String>>>()

    data class AutogenData(
        val identifier: String,
        val properties: BlockProperties,
        val components: MutableList<Pair<String, String>>
    )

    fun run() {
        log.info("Starting BlockDefinitionGenerator")
        populateFromProperties(PROPERTIES)
        populateFromInstances(CACHE_CONSTRUCTORS.map {
            try { it.value(null) }
            catch (_: Throwable) {
                log.error("Error occurred while creating instance of ${it.key}")
                null
            }
        }.filterNotNull())
        buildDefinitions()
        log.info("Finished BlockDefinitionGenerator")
    }

    fun populateFromClasses(classes: Map<String, KClass<out Block>>) {

    }

    fun populateFromInstances(instances: List<Block>) {
        instances.forEach {
            val components = this.components.computeIfAbsent(it.properties.identifier) { mutableListOf() }

            val componentGenerators: MutableList<Pair<String, () -> String?>> = mutableListOf(
                Pair("SolidComponent") { if (!it.isSolid) "(solid = false)" else null },
                Pair("TransparentComponent") { if (it.isTransparent) "(transparent = true)" else null },
                Pair("MapColorComponent") {
                    if (it.color != BlockColor.VOID_BLOCK_COLOR) {
                        with(it.color) {
                            "(r = $red, g = $green, b = $blue, a = $alpha)"
                        }
                    } else null
                },
                Pair("FrictionComponent") {
                    if (it.frictionFactor != 0.6) "(friction = ${it.frictionFactor}f)" else null
                },
                Pair("InternalFrictionComponent") {
                    if (it.passableBlockFrictionFactor != 1.0) "(internalFriction = ${it.passableBlockFrictionFactor}f)" else null
                },
                Pair("LightEmissionComponent") {
                    if (it.lightLevel != 0) "(emission = ${it.lightLevel})" else null
                },
                Pair("LightDampeningComponent") {
                    if (it.lightFilter != (if (it.isSolid && !it.isTransparent) 15 else 1)) {
                        "(dampening = ${it.lightFilter})"
                    } else null
                },
                Pair("ReplaceableComponent") {
                    if (it.canBeReplaced()) "" else null
                },
                Pair("FlammableComponent") {
                    if (it.burnChance != 0 || it.burnAbility != 0) "(catchChance = ${it.burnChance}, destroyChance = ${it.burnAbility})"
                    else null
                },
                Pair("MineableComponent") {
                    if (it.hardness != 10.0) "(hardness = ${it.hardness}f)" else null
                },
                Pair("MoveableComponent") {
                    if (!it.canBePushed() || (!it.canBePulled() || !it.sticksToPiston()) || it.breaksWhenMoved() || it.canSticksBlock()) {
                        val push = it.canBePushed()
                        val pull = (it.canBePulled() || it.sticksToPiston())
                        val movement = if (it.breaksWhenMoved()) {
                            "MoveableComponent.Movement.Break"
                        } else if (push && pull) {
                            "MoveableComponent.Movement.Both"
                        } else if (push) {
                            "MoveableComponent.Movement.Push"
                        } else {
                            "MoveableComponent.Movement.None"
                        }

                        val sticky = it.canSticksBlock()

                        "(movement = $movement, sticky = $sticky)"
                    } else null
                }
            )

            componentGenerators.forEach { gen ->
                val pair = try {
                    val generated = gen.second() ?: return@forEach
                    gen.first to generated
                } catch (t: Throwable) {
                    log.error("Error occurred while generating \"${gen.first}\" for \"${it.properties.identifier}\" from instance, inserting TODO")
                    "TODO" to "(\"${gen.first}\")"
                }
                components.add(pair)
            }
        }
    }

    fun populateFromProperties(properties: Map<String, BlockProperties>) {
        this.properties += properties
    }

    fun buildDefinitions() {
        val autogenData = mutableListOf<AutogenData>()

        this.properties.forEach {
            val components = this.components[it.key] ?: mutableListOf()

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

        val formattedComponents = components.joinToString(", ") {
            "${it.first}${it.second}"
        }

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
            components.forEach {
                if (it.first != "TODO") fileBuilder.addImport("org.chorus_oss.chorus.experimental.block.components", it.first)
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