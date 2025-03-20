package org.chorus.tags

import com.google.gson.reflect.TypeToken
import org.chorus.Server
import org.chorus.utils.JSONUtils
import org.jetbrains.annotations.UnmodifiableView
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

object BlockTags {
    const val ACACIA: String = "acacia"
    const val BIRCH: String = "birch"
    const val DARK_OAK: String = "dark_oak"
    const val DIAMOND_PICK_DIGGABLE: String = "diamond_pick_diggable"
    const val DIRT: String = "dirt"
    const val FERTILIZE_AREA: String = "fertilize_area"
    const val GOLD_PICK_DIGGABLE: String = "gold_pick_diggable"
    const val GRASS: String = "grass"
    const val GRAVEL: String = "gravel"
    const val IRON_PICK_DIGGABLE: String = "iron_pick_diggable"
    const val JUNGLE: String = "jungle"
    const val LOG: String = "log"
    const val METAL: String = "metal"
    const val MINECRAFT_CROP: String = "minecraft:crop"
    const val MOB_SPAWNER: String = "mob_spawner"
    const val NOT_FEATURE_REPLACEABLE: String = "not_feature_replaceable"
    const val OAK: String = "oak"
    const val PLANT: String = "plant"
    const val PUMPKIN: String = "pumpkin"
    const val RAIL: String = "rail"
    const val SAND: String = "sand"
    const val SNOW: String = "snow"
    const val SPRUCE: String = "spruce"
    const val STONE: String = "stone"
    const val STONE_PICK_DIGGABLE: String = "stone_pick_diggable"
    const val TEXT_SIGN: String = "text_sign"
    const val WATER: String = "water"
    const val WOOD: String = "wood"
    const val WOOD_PICK_DIGGABLE: String = "wood_pick_diggable"

    //PNX only
    const val PNX_WOOL: String = "pnx:wool"
    const val PNX_SHULKERBOX: String = "pnx:shulkerbox"


    private val TAG_2_BLOCKS = HashMap<String, MutableSet<String>>()
    private val BLOCKS_2_TAGS = HashMap<String, MutableSet<String>>()

    init {
        try {
            Server::class.java.classLoader.getResourceAsStream("block_tags.json").use { stream ->
                val typeToken: TypeToken<HashMap<String, HashSet<String>>> =
                    object : TypeToken<HashMap<String, HashSet<String>>>() {}
                checkNotNull(stream)
                val map = JSONUtils.from(InputStreamReader(stream), typeToken)
                val map2 = HashMap<String, HashSet<String>>()
                map.forEach { (key, value) ->
                    val handle: HashSet<String> = HashSet(value.size)
                    handle.addAll(value)
                    map2[key] = handle
                }
                TAG_2_BLOCKS.putAll(map2)
                for ((key, value) in TAG_2_BLOCKS) {
                    for (block in value) {
                        val tags = BLOCKS_2_TAGS.computeIfAbsent(
                            block
                        ) { HashSet() }
                        tags.add(key)
                    }
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun getTagSet(identifier: String?): @UnmodifiableView MutableSet<String> {
        return Collections.unmodifiableSet(BLOCKS_2_TAGS.getOrDefault(identifier, setOf()))
    }

    fun getBlockSet(tag: String?): @UnmodifiableView MutableSet<String> {
        return Collections.unmodifiableSet(TAG_2_BLOCKS.getOrDefault(tag, setOf()))
    }

    /**
     * Register Block tags for the given block identifier.
     * This is a server-side only method, DO NOT affect the client.
     *
     * @param identifier The block identifier
     * @param tags       The tags to register
     */
    fun register(identifier: String, tags: Collection<String>) {
        val tagSet = BLOCKS_2_TAGS[identifier]
        if (tagSet == null) {
            BLOCKS_2_TAGS[identifier] = HashSet(tags)
        } else tagSet.addAll(tags)

        for (tag in tags) {
            val itemSet = TAG_2_BLOCKS[tag]
            if (itemSet == null) {
                TAG_2_BLOCKS[tag] = HashSet(setOf(identifier))
            } else itemSet.add(identifier)
        }
    }
}
