package org.chorus.level.generator.terra.mappings

import cn.nukkit.entity.data.EntityDataMap.put
import cn.nukkit.level.generator.terra.mappings.DoubleKeyMappedRegistry.MapPair

/**
 * Allay Project 2023/10/27
 *
 * @author daoge_cmd
 */
interface DoubleKeyMappedRegistry<K1, K2, VALUE> :
    Registry<MapPair<K1, K2, VALUE>?> {
    @JvmRecord
    data class MapPair<K1, K2, VALUE>(val m1: Map<K1, VALUE>, val m2: Map<K2, VALUE>)

    fun getByK1(k1: K1): VALUE? {
        return content.m1[k1]
    }

    fun getByK2(k2: K2): VALUE? {
        return content.m2[k2]
    }

    fun getByK1OrDefault(k1: K1, defaultValue: VALUE): VALUE {
        return content.m1.getOrDefault(k1, defaultValue)
    }

    fun getByK2OrDefault(k2: K2, defaultValue: VALUE): VALUE {
        return content.m2.getOrDefault(k2, defaultValue)
    }

    fun register(k1: K1, k2: K2, value: VALUE) {
        val content: MapPair<K1?, K2?, VALUE?>? = getContent()
        content!!.m1.put(k1, value)
        content.m2.put(k2, value)
    }
}
