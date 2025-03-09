package org.chorus.level.generator.terra.mappings

import org.chorus.entity.data.EntityDataMap.put
import java.util.*
import java.util.function.Function

/**
 * An abstract registry holding a map of various registrations as defined by [MAPPING].
 * The M represents the map class, which can be anything that extends [Map]. The
 * [KEY] and [VALUE] generics are the key and value respectively.
 *
 * @param <KEY>     the key
 * @param <VALUE>   the value
 * @param <MAPPING> the map
 *
 *
 * @author daoge_cmd <br></br>
 * Date: 2023/3/18 <br></br>
 * Allay Project <br></br>
</MAPPING></VALUE></KEY> */
interface MappedRegistry<KEY, VALUE, MAPPING : Map<KEY, VALUE>?> :
    Registry<MAPPING> {
    /**
     * Returns the value registered by the given key.
     *
     * @param key the key
     * @return the value registered by the given key.
     */
    fun get(key: KEY): VALUE? {
        return content.get(key)
    }

    /**
     * Returns and maps the value by the given key if present.
     *
     * @param key    the key
     * @param mapper the mapper
     * @param <U>    the type
     * @return the mapped value from the given key if present
    </U> */
    fun <U> map(key: KEY, mapper: Function<in VALUE, out U>): Optional<U> {
        val value = this.get(key)
        return if (value == null) {
            Optional.empty()
        } else {
            Optional.ofNullable(mapper.apply(value))
        }
    }

    /**
     * Returns the value registered by the given key or the default value
     * specified if null.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the value registered by the given key or the default value
     * specified if null.
     */
    fun getOrDefault(key: KEY, defaultValue: VALUE): VALUE {
        return content.getOrDefault(key, defaultValue)
    }

    /**
     * Registers a new value into this registry with the given key.
     *
     * @param key   the key
     * @param value the value
     * @return a new value into this registry with the given key.
     */
    fun register(key: KEY, value: VALUE): VALUE? {
        return content.put(key, value)
    }
}
