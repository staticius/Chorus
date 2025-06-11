package org.chorus_oss.chorus.registry

import com.google.gson.GsonBuilder
import org.chorus_oss.chorus.nbt.NBTIO.readTreeMapCompoundTag
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.nbt.tag.ListTag
import org.chorus_oss.chorus.nbt.tag.StringTag
import org.chorus_oss.chorus.nbt.tag.Tag
import org.chorus_oss.chorus.registry.BiomeRegistry.BiomeDefinition
import org.jetbrains.annotations.UnmodifiableView
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Collectors

class BiomeRegistry : IRegistry<Int, BiomeDefinition?, BiomeDefinition> {
    override fun init() {
        if (isLoad.getAndSet(true)) return
        try {
            BiomeRegistry::class.java.classLoader.getResourceAsStream("biome_id_and_type.json").use { stream ->
                if (stream == null) throw RuntimeException("Could not load biome_id_and_type.json")

                val gson = GsonBuilder().setObjectToNumberStrategy { it.nextInt() }.create()
                val map: Map<String, *> = gson.fromJson<Map<String, *>>(
                    InputStreamReader(stream),
                    MutableMap::class.java
                )
                for ((key, value) in map) {
                    NAME2ID[key] = (value as Map<*, *>)["id"].toString().toInt()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        // TODO: New Biome Stuff

//        try {
//            BiomeRegistry::class.java.classLoader.getResourceAsStream("biome_definitions.nbt").use { stream ->
//                stream ?: throw RuntimeException("Could not load biome_definitions.nbt")
//                val compoundTag = readTreeMapCompoundTag(stream, ByteOrder.BIG_ENDIAN, true)
//                val tags: Map<String, Tag<*>> = compoundTag.tags
//                for ((key, value1) in tags) {
//                    val id = NAME2ID[key]!!
//                    val value = value1 as CompoundTag?
//                    val tags1 = value!!.getList("tags", StringTag::class.java)
//                    val list =
//                        tags1.all.stream().map { obj: StringTag? -> obj!!.parseValue() }.collect(Collectors.toSet())
//                    val biomeDefinition = BiomeDefinition(
//                        value.getFloat("ash"),
//                        value.getFloat("blue_spores"),
//                        value.getFloat("depth"),
//                        value.getFloat("downfall"),
//                        value.getFloat("height"),
//                        value.getString("name_hash"),
//                        value.getByte("rain"),
//                        value.getFloat("red_spores"),
//                        list,
//                        value.getFloat("temperature"),
//                        value.getFloat("waterColorA"),
//                        value.getFloat("waterColorB"),
//                        value.getFloat("waterColorG"),
//                        value.getFloat("waterColorR"),
//                        value.getFloat("waterTransparency"),
//                        value.getFloat("white_ash")
//                    )
//                    register(id, biomeDefinition)
//                }
//            }
//        } catch (e: IOException) {
//            throw RuntimeException(e)
//        } catch (e: RegisterException) {
//            throw RuntimeException(e)
//        }
    }

    override operator fun get(key: Int): BiomeDefinition? {
        return DEFINITIONS[key]
    }

    fun get(biomeName: String): BiomeDefinition? {
        return get(NAME2ID[biomeName] ?: return null)
    }

    fun getBiomeId(biomeName: String): Int? {
        return NAME2ID[biomeName]
    }

    val biomeDefinitionListPacketData: ByteArray
        get() {
            // TODO: Figure out the mapping of custom biomes
            try {
                BiomeRegistry::class.java.classLoader.getResourceAsStream("biome_definitions.nbt")
                    .use { resourceAsStream ->
                        checkNotNull(resourceAsStream)
                        return resourceAsStream.readAllBytes()
                    }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

    val biomeDefinitions: @UnmodifiableView MutableSet<BiomeDefinition>
        get() = Collections.unmodifiableSet(HashSet(DEFINITIONS.values))

    override fun reload() {
        isLoad.set(false)
        DEFINITIONS.clear()
        REGISTRY.clear()
        NAME2ID.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(key: Int, value: BiomeDefinition) {
        if (DEFINITIONS.putIfAbsent(key, value) == null) {
            NAME2ID[value.nameHash] = key
            REGISTRY.add(value.toNBT())
        } else {
            throw RegisterException("This biome has already been registered with the id: $key")
        }
    }

    @JvmRecord
    data class BiomeDefinition(
        val ash: Float,
        val blueSpores: Float,
        val depth: Float,
        val downfall: Float,
        val height: Float,
        @JvmField val nameHash: String,
        val rain: Byte,
        val redSpores: Float,
        @JvmField val tags: MutableSet<String>,
        val temperature: Float,
        val waterColorA: Float,
        val waterColorB: Float,
        val waterColorG: Float,
        val waterColorR: Float,
        val waterTransparency: Float,
        val whiteAsh: Float
    ) {
        fun tags(): @UnmodifiableView MutableSet<String> {
            return Collections.unmodifiableSet(tags)
        }

        fun toNBT(): CompoundTag {
            val stringTags = ListTag<StringTag>()
            for (s in tags) {
                stringTags.add(StringTag(s))
            }
            return CompoundTag()
                .putFloat("ash", ash)
                .putFloat("blue_spores", blueSpores)
                .putFloat("depth", depth)
                .putFloat("downfall", downfall)
                .putFloat("height", height)
                .putString("name_hash", nameHash)
                .putByte("rain", rain.toInt())
                .putFloat("red_spores", redSpores)
                .putList("tags", stringTags)
                .putFloat("temperature", temperature)
                .putFloat("waterColorA", waterColorA)
                .putFloat("waterColorB", waterColorB)
                .putFloat("waterColorG", waterColorG)
                .putFloat("waterColorG", waterColorG)
                .putFloat("waterColorR", waterColorR)
                .putFloat("waterTransparency", waterTransparency)
                .putFloat("white_ash", whiteAsh)
        }
    }

    companion object {
        private val DEFINITIONS = HashMap<Int, BiomeDefinition>(0xFF)
        private val NAME2ID = HashMap<String, Int>(0xFF)
        private val REGISTRY: MutableList<CompoundTag> = mutableListOf()
        private val isLoad = AtomicBoolean(false)
    }
}
