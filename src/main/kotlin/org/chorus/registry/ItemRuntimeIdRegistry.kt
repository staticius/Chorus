package org.chorus.registry

import org.chorus.registry.RegisterException
import org.chorus.utils.BinaryStream
import com.google.gson.JsonParser
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import lombok.Getter
import lombok.extern.slf4j.Slf4j
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Cool_Loong
 */

class ItemRuntimeIdRegistry : IRegistry<String, Int, Int> {
    val itemPalette: ByteArray
        get() = Companion.itemPalette

    private fun generatePalette() {
        val paletteBuffer = BinaryStream()
        val verify = HashMap<Int, Boolean?>()
        paletteBuffer.putUnsignedVarInt((REGISTRY.size() + CUSTOM_REGISTRY.size()).toLong())
        for (entry in REGISTRY.object2IntEntrySet()) {
            paletteBuffer.putString(entry.key)
            val rid = entry.intValue
            paletteBuffer.putLShort(rid)
            require(verify.putIfAbsent(rid, true) == null) { "Runtime ID is already registered: $rid" }
            paletteBuffer.putBoolean(false) //Vanilla Item doesnt component item
        }
        for ((key, value) in CUSTOM_REGISTRY.object2ObjectEntrySet()) {
            paletteBuffer.putString(key)
            val rid = value!!.runtimeId
            paletteBuffer.putLShort(rid)
            require(verify.putIfAbsent(rid, true) == null) { "Runtime ID is already registered: $rid" }
            paletteBuffer.putBoolean(value.isComponent)
        }
        Companion.itemPalette = paletteBuffer.buffer
    }

    override fun init() {
        if (isLoad.getAndSet(true)) return

        // We use ProxyPass data since protocol 776 since we need item version and componentBased now.
        try {
            ItemRegistry::class.java.classLoader.getResourceAsStream("runtime_item_states.json").use { stream ->
                if (stream == null) {
                    throw RuntimeException("Failed to load runtime_item_states.json")
                }
                val items = JsonParser.parseReader(InputStreamReader(stream)).asJsonArray

                for (element in items) {
                    val item = element.asJsonObject
                    register1(
                        ItemData(
                            item["name"].asString,
                            item["id"].asInt,
                            item["version"].asInt,
                            item["componentBased"].asBoolean
                        )
                    )
                }
                trim()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun get(key: String): Int {
        val i = REGISTRY.getInt(key)
        if (i == Int.MAX_VALUE) {
            val runtimeEntry = CUSTOM_REGISTRY[key]
            return runtimeEntry?.runtimeId ?: Int.MAX_VALUE
        }
        return i
    }

    fun getInt(key: String?): Int {
        val i = REGISTRY.getInt(key)
        if (i == Int.MAX_VALUE) {
            val runtimeEntry = CUSTOM_REGISTRY[key]
            return runtimeEntry?.runtimeId ?: Int.MAX_VALUE
        }
        return i
    }

    fun getIdentifier(runtimeId: Int): String? {
        return ID2NAME[runtimeId]
    }

    override fun trim() {
        REGISTRY.trim()
        CUSTOM_REGISTRY.trim()
        generatePalette()
    }

    override fun reload() {
        isLoad.set(false)
        REGISTRY.clear()
        CUSTOM_REGISTRY.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(key: String, value: Int) {
        if (REGISTRY.putIfAbsent(key, value) == Int.MAX_VALUE) {
            ID2NAME.put(value, key)
        } else {
            throw RegisterException("The item: " + key + "runtime id has been registered!")
        }
    }

    @Throws(RegisterException::class)
    fun registerCustomRuntimeItem(entry: RuntimeEntry) {
        if (CUSTOM_REGISTRY.putIfAbsent(entry.identifier, entry) == null) {
            ID2NAME.put(entry.runtimeId, entry.identifier)
            ITEMDATA.add(ItemData(entry.identifier, entry.runtimeId, 1, entry.isComponent))
        } else {
            throw RegisterException("The item: " + entry.identifier + " runtime id has been registered!")
        }
    }

    private fun register0(key: String?, value: Int) {
        if (REGISTRY.putIfAbsent(key, value) == Int.MAX_VALUE) {
            ID2NAME.put(value, key)
        }
    }

    fun register1(entry: ItemData) {
        if (!ITEMDATA.contains(entry)) {
            ITEMDATA.add(entry)
            register0(entry.identifier, entry.runtimeId)
        }
    }

    @JvmRecord
    data class RuntimeEntry(val identifier: String?, val runtimeId: Int, val isComponent: Boolean)

    @JvmRecord
    data class ItemData(val identifier: String?, val runtimeId: Int, val version: Int, val componentBased: Boolean)
    companion object {
        private val isLoad = AtomicBoolean(false)

        private val REGISTRY = Object2IntOpenHashMap<String?>()
        private val CUSTOM_REGISTRY = Object2ObjectOpenHashMap<String?, RuntimeEntry?>()

        init {
            REGISTRY.defaultReturnValue(Int.MAX_VALUE)
        }

        private val ID2NAME = Int2ObjectOpenHashMap<String?>()

        
        private val ITEMDATA = ObjectOpenHashSet<ItemData>()

        private var itemPalette: ByteArray
    }
}
