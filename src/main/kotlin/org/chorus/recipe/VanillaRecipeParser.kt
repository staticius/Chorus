package org.chorus.recipe

import org.chorus.entity.effect.PotionType
import org.chorus.entity.effect.PotionType.Companion.get
import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemPotion.Companion.fromPotion
import org.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus.recipe.descriptor.ItemDescriptor
import org.chorus.recipe.descriptor.ItemTagDescriptor
import org.chorus.registry.RecipeRegistry
import org.chorus.registry.Registries
import org.chorus.utils.JSONUtils
import com.google.gson.reflect.TypeToken
import lombok.extern.slf4j.Slf4j
import java.io.*
import java.util.function.Consumer
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.component1
import kotlin.collections.dropLastWhile
import kotlin.collections.forEach
import kotlin.collections.listOf
import kotlin.collections.set
import kotlin.collections.toTypedArray
import kotlin.math.max


class VanillaRecipeParser(private val recipeRegistry: RecipeRegistry) {
    fun parseAndRegisterRecipe(file: File) {
        try {
            FileReader(file).use { reader ->
                matchAndParse(reader)
            }
        } catch (e: IOException) {
            VanillaRecipeParser.log.error(e.message)
        }
    }

    fun parseAndRegisterRecipe(file: InputStream) {
        try {
            InputStreamReader(file).use { reader ->
                matchAndParse(reader)
            }
        } catch (e: IOException) {
            VanillaRecipeParser.log.error(e.message)
        }
    }

    private fun matchAndParse(reader: InputStreamReader) {
        val map: Map<String, Any> = JSONUtils.from<Map<String, Any>>(reader, object : TypeToken<Map<String?, Any?>?>() {
        })
        if (map.containsKey(SHAPED_KEY)) {
            parseAndRegisterShapedRecipe((map[SHAPED_KEY] as Map<String, Any>?)!!)
        } else if (map.containsKey(SHAPELESS_KEY)) {
            parseAndRegisterShapeLessRecipe((map[SHAPELESS_KEY] as Map<String, Any>?)!!)
        } else if (map.containsKey(FURNACE_KEY)) {
            parseAndRegisterFurnaceRecipe((map[FURNACE_KEY] as Map<String, Any>?)!!)
        } else if (map.containsKey(BREW_KEY)) {
            parseAndRegisterBrewRecipe((map[BREW_KEY] as Map<String, Any>?)!!)
        } else if (map.containsKey(CONTAINER_KEY)) {
            parseAndRegisterContainerRecipe((map[CONTAINER_KEY] as Map<String, Any>?)!!)
        }
    }

    private fun parseAndRegisterShapedRecipe(recipeData: Map<String, Any>) {
        val tags = tags(recipeData)
        if (tags!!.size == 1 && tags[0] == "crafting_table") {
            val prior = recipeData.getOrDefault("priority", 0) as Int
            val pattern = recipeData["pattern"] as List<String>?
            val shapes: Array<String>
            if (pattern!!.size > 1) {
                val maxWidth = pattern.stream().map { s: String -> s.toCharArray().size }.max { x: Int?, y: Int? ->
                    Integer.compare(
                        x!!,
                        y!!
                    )
                }.get()
                shapes = pattern.stream().map<String> { shape: String ->
                    val builder = StringBuilder()
                    val charArray = shape.toCharArray()
                    for (c in charArray) {
                        builder.append(c)
                    }
                    builder.append(" ".repeat(max(0.0, (maxWidth - charArray.size).toDouble()).toInt())).toString()
                }.toArray<String> { _Dummy_.__Array__() }
            } else {
                shapes = pattern.toArray<String> { _Dummy_.__Array__() }
            }
            val key = recipeData["key"] as Map<String, Map<String, Any>>?
            val ingredients: MutableMap<Char, ItemDescriptor> = LinkedHashMap()
            try {
                key!!.forEach { (k: String?, v: MutableMap<String?, Any?>?) ->
                    if (v.containsKey("tag")) {
                        val tag: String = v.get("tag").toString()
                        val count = v.getOrDefault("count", 1) as Int
                        ingredients[k[0]] = ItemTagDescriptor(tag, count)
                    } else {
                        ingredients[k[0]] = DefaultDescriptor(parseItem(v))
                    }
                }
                val o = recipeData["result"]
                var result = java.util.Map.of<String?, Any>()
                if (o is Map<*, *>) {
                    result = o as Map<String?, Any>
                } else if (o is List<*>) {
                    result = o[0] as Map<String?, Any>
                }
                Registries.RECIPE.register(
                    ShapedRecipe(
                        description(recipeData),
                        prior,
                        parseItem(result),
                        shapes,
                        ingredients,
                        listOf()
                    )
                )
            } catch (ignore: AssertionError) {
            }
        }
    }

    private fun parseAndRegisterShapeLessRecipe(recipeData: Map<String, Any>) {
        val prior = recipeData.getOrDefault("priority", 0) as Int
        val ingredients = recipeData["ingredients"] as List<Map<String?, Any>>?
        val itemDescriptors: MutableList<ItemDescriptor> = ArrayList()
        try {
            ingredients!!.forEach(Consumer { v: Map<String?, Any> ->
                if (v.containsKey("tag")) {
                    val tag = v["tag"].toString()
                    val count = v.getOrDefault("count", 1) as Int
                    itemDescriptors.add(ItemTagDescriptor(tag, count))
                } else {
                    itemDescriptors.add(DefaultDescriptor(parseItem(v)))
                }
            })
            val result = recipeData["result"] as Map<String?, Any>?
            val re = parseItem(result!!)
            val tags = tags(recipeData)
            for (tag in tags!!) {
                val recipe: Recipe = when (tag) {
                    CRAFTING_TABLE_TAG -> ShapelessRecipe(description(recipeData), prior, re, itemDescriptors)
                    SHULKER_BOX_TAG -> UserDataShapelessRecipe(description(recipeData), prior, re, itemDescriptors)
                    STONE_CUTTER_TAG -> StonecutterRecipe(
                        description(recipeData),
                        prior,
                        re,
                        itemDescriptors[0].toItem()
                    )

                    CARTOGRAPHY_TABLE_TAG -> CartographyRecipe(description(recipeData), prior, re, itemDescriptors)
                    else -> throw IllegalArgumentException(tag)
                }
                Registries.RECIPE.register(recipe)
            }
        } catch (ignore: AssertionError) {
        }
    }

    private fun parseAndRegisterFurnaceRecipe(recipeData: Map<String, Any>) {
        val input = get(recipeData["input"].toString())
        val output = get(recipeData["output"].toString())
        if (input.isNull || output.isNull) {
            return
        }
        val tags = tags(recipeData)
        for (tag in tags!!) {
            val recipe: Recipe = when (tag) {
                FURNACE_TAG -> FurnaceRecipe(description(recipeData), output, input)
                SMOKER_TAG -> SmokerRecipe(description(recipeData), output, input)
                BLAST_FURNACE_TAG -> BlastFurnaceRecipe(description(recipeData), output, input)
                CAMPFIRE_TAG, SOUL_CAMPFIRE_TAG -> CampfireRecipe(description(recipeData), output, input)
                else -> throw IllegalArgumentException(tag)
            }
            Registries.RECIPE.register(recipe)
        }
    }

    private fun parseAndRegisterBrewRecipe(recipeData: Map<String, Any>) {
        val inputID = "minecraft:" + recipeData["input"].toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[2].lowercase()
        val input: Item = fromPotion(PotionType.get(inputID))
        val outputID = "minecraft:" + recipeData["output"].toString().split(":".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray()[2].lowercase()
        val output: Item = fromPotion(PotionType.get(outputID))
        val reagent = get(recipeData["reagent"].toString())
        if (input.isNull || output.isNull || reagent.isNull) {
            return
        }
        val tags = tags(recipeData)
        if (tags!![0] == BREW_STAND_TAG) {
            Registries.RECIPE.register(BrewingRecipe(description(recipeData), input, reagent, output))
        }
    }

    private fun parseAndRegisterContainerRecipe(recipeData: Map<String, Any>) {
        val input = get(recipeData["input"].toString())
        val output = get(recipeData["output"].toString())
        val reagent = get(recipeData["reagent"].toString())
        if (input.isNull || output.isNull || reagent.isNull) {
            return
        }
        val tags = tags(recipeData)
        if (tags!![0] == BREW_STAND_TAG) {
            Registries.RECIPE.register(ContainerRecipe(description(recipeData), input, reagent, output))
        }
    }

    @Throws(AssertionError::class)
    private fun parseItem(v: Map<String?, Any>): Item {
        val item = v["item"] as String?
        val count = v.getOrDefault("count", 1) as Int
        val data = v.getOrDefault("data", 32767) as Int
        var i = get(item!!)
        if (i.isNull) {
            throw AssertionError()
        }
        if (data != 0) {
            if (data == 32767) {
                i = i.clone()
                i.disableMeta()
            } else {
                i.damage = data
            }
        }
        i.setCount(count)
        return i
    }

    private fun description(recipeData: Map<String, Any>): String? {
        return (recipeData["description"] as Map<String?, String?>)["identifier"]
    }

    private fun tags(recipeData: Map<String, Any>): List<String>? {
        return recipeData["tags"] as List<String>?
    }

    companion object {
        private const val SHAPED_KEY = "minecraft:recipe_shaped"
        private const val SHAPELESS_KEY = "minecraft:recipe_shapeless"
        private const val FURNACE_KEY = "minecraft:recipe_furnace"
        private const val BREW_KEY = "minecraft:recipe_brewing_mix"
        private const val CONTAINER_KEY = "minecraft:recipe_brewing_container"

        private const val STONE_CUTTER_TAG = "stonecutter"
        private const val CRAFTING_TABLE_TAG = "crafting_table"
        private const val SHULKER_BOX_TAG = "shulker_box"
        private const val CARTOGRAPHY_TABLE_TAG = "cartography_table"
        private const val FURNACE_TAG = "furnace"
        private const val SMOKER_TAG = "smoker"
        private const val BLAST_FURNACE_TAG = "blast_furnace"
        private const val CAMPFIRE_TAG = "campfire"
        private const val SOUL_CAMPFIRE_TAG = "soul_campfire"
        private const val BREW_STAND_TAG = "brewing_stand"
    }
}
