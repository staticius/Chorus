package org.chorus.registry

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.chorus.nbt.NBTIO.readTreeMapCompoundTag
import org.chorus.nbt.tag.*
import org.chorus.registry.BiomeRegistry.BiomeDefinition
import org.chorus.registry.RegisterException
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
                val gson = GsonBuilder().setObjectToNumberStrategy { obj: JsonReader -> obj.nextInt() }
                    .create()
                val map: Map<String?, *> = gson.fromJson<Map<*, *>>(
                    InputStreamReader(stream),
                    MutableMap::class.java
                )
                for ((key, value) in map) {
                    NAME2ID.put(key, (value as Map<String?, *>)["id"].toString().toInt())
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        try {
            BiomeRegistry::class.java.classLoader.getResourceAsStream("biome_definitions.nbt").use { stream ->
                val compoundTag = readTreeMapCompoundTag(stream, ByteOrder.BIG_ENDIAN, true)
                val tags: Map<String?, Tag?> = compoundTag.getTags()
                for ((key, value1) in tags) {
                    val id = NAME2ID.getInt(key)
                    val value = value1 as CompoundTag?
                    val tags1 = value!!.getList("tags", StringTag::class.java)
                    val list =
                        tags1!!.all.stream().map { obj: StringTag? -> obj!!.parseValue() }.collect(Collectors.toSet())
                    val biomeDefinition = BiomeDefinition(
                        value.getFloat("ash"),
                        value.getFloat("blue_spores"),
                        value.getFloat("depth"),
                        value.getFloat("downfall"),
                        value.getFloat("height"),
                        value.getString("name_hash"),
                        value.getByte("rain"),
                        value.getFloat("red_spores"),
                        list,
                        value.getFloat("temperature"),
                        value.getFloat("waterColorA"),
                        value.getFloat("waterColorB"),
                        value.getFloat("waterColorG"),
                        value.getFloat("waterColorR"),
                        value.getFloat("waterTransparency"),
                        value.getFloat("white_ash")
                    )
                    register(id, biomeDefinition)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: RegisterException) {
            throw RuntimeException(e)
        }
    }

    override operator fun get(key: Int): BiomeDefinition? {
        return DEFINITIONS[key]
    }

    fun get(biomeName: String?): BiomeDefinition? {
        return get(NAME2ID.getInt(biomeName))
    }

    fun getBiomeId(biomeName: String?): Int {
        return NAME2ID.getInt(biomeName)
    }

    val biomeDefinitionListPacketData: ByteArray
        get() {
            //todo Figure out the mapping of custom biomes
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

    val biomeDefinitions: @UnmodifiableView MutableSet<BiomeDefinition?>
        get() = Collections.unmodifiableSet(HashSet(DEFINITIONS.values))

    override fun trim() {
        DEFINITIONS.trim()
        NAME2ID.trim()
    }

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
            NAME2ID.put(value.name_hash, key)
            REGISTRY.add(value.toNBT())
        } else {
            throw RegisterException("This biome has already been registered with the id: $key")
        }
    }

    @JvmRecord
    data class BiomeDefinition(
        val ash: Float,
        val blue_spores: Float,
        val depth: Float,
        val downfall: Float,
        val height: Float,
        @JvmField val name_hash: String?,
        val rain: Byte,
        val red_spores: Float,
        @JvmField val tags: Set<String>,
        val temperature: Float,
        val waterColorA: Float,
        val waterColorB: Float,
        val waterColorG: Float,
        val waterColorR: Float,
        val waterTransparency: Float,
        val white_ash: Float
    ) {
        override fun tags(): @UnmodifiableView MutableSet<String> {
            return Collections.unmodifiableSet(tags)
        }

        fun toNBT(): CompoundTag {
            val stringTagListTag = ListTag<StringTag?>()
            for (s in tags) {
                stringTagListTag.add(StringTag(s))
            }
            return CompoundTag()
                .putFloat("ash", ash)
                .putFloat("blue_spores", blue_spores)
                .putFloat("depth", depth)
                .putFloat("downfall", downfall)
                .putFloat("height", height)
                .putString("name_hash", name_hash!!)
                .putByte("rain", rain.toInt())
                .putFloat("red_spores", red_spores)
                .putList("tags", stringTagListTag)
                .putFloat("temperature", temperature)
                .putFloat("waterColorA", waterColorA)
                .putFloat("waterColorB", waterColorB)
                .putFloat("waterColorG", waterColorG)
                .putFloat("waterColorG", waterColorG)
                .putFloat("waterColorR", waterColorR)
                .putFloat("waterTransparency", waterTransparency)
                .putFloat("white_ash", white_ash)
        }
    }

    companion object {
        private val DEFINITIONS = Int2ObjectOpenHashMap<BiomeDefinition?>(0xFF)
        private val NAME2ID = Object2IntOpenHashMap<String?>(0xFF)
        private val REGISTRY: MutableList<CompoundTag?> = ArrayList(0xFF)
        private val isLoad = AtomicBoolean(false)
    }
}
