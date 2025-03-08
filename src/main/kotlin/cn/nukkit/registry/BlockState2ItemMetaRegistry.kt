package cn.nukkit.registry

import cn.nukkit.nbt.NBTIO.readCompressed
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.IntTag
import cn.nukkit.registry.RegisterException
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Cool_Loong
 */
class BlockState2ItemMetaRegistry : IRegistry<String, Int, Int> {
    override fun init() {
        if (isLoad.getAndSet(true)) return
        try {
            BlockState2ItemMetaRegistry::class.java.classLoader.getResourceAsStream("item_meta_block_state_bimap.nbt")
                .use { input ->
                    val compoundTag = readCompressed(input)
                    for ((key, value) in compoundTag.getTags()) {
                        for ((key1, value1) in (value as CompoundTag).getTags()) {
                            MAP.put("$key#$key1", (value1 as IntTag).getData().intValue())
                        }
                    }
                }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun reload() {
        isLoad.set(false)
        MAP.clear()
        init()
    }

    override fun get(key: String): Int {
        return MAP[key]!!
    }

    fun get(key: String, meta: Int): Int {
        return MAP.getInt("$key#$meta")
    }

    override fun trim() {
        MAP.trim()
    }

    @Throws(RegisterException::class)
    override fun register(key: String, value: Int) {
        if (MAP.putIfAbsent(key, value) == 0) {
        } else {
            throw RegisterException("The mapping has been registered!")
        }
    }

    companion object {
        //blockid#meta -> blockhash
        private val MAP = Object2IntOpenHashMap<String>()
        private val isLoad = AtomicBoolean(false)
    }
}
