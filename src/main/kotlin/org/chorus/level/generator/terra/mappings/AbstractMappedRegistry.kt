package org.chorus.level.generator.terra.mappings

import org.chorus.entity.data.EntityDataMap.put
import java.util.*
import java.util.function.Function


/**
 * An abstract registry holding a map of various registrations as defined by [M].
 * The M represents the map class, which can be anything that extends [Map]. The
 * [K] and [V] generics are the key and value respectively.
 *
 * @param <K> the key
 * @param <V> the value
 * @param <M> the map
</M></V></K> */
abstract class AbstractMappedRegistry<K, V, M : Map<K, V>?> protected constructor(
    input: I?,
    registryLoader: RegistryLoader<I?, M>
) :
    MappingRegistry<M>(input, registryLoader) {
    /**
     * Returns the value registered by the given key.
     *
     * @param key the key
     * @return the value registered by the given key.
     */
    fun get(key: K): V? {
        return mappings!![key]
    }

    /**
     * Returns and maps the value by the given key if present.
     *
     * @param key    the key
     * @param mapper the mapper
     * @param <U>    the type
     * @return the mapped value from the given key if present
    </U> */
    fun <U> map(key: K, mapper: Function<in V, out U>): Optional<U> {
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
    fun getOrDefault(key: K, defaultValue: V): V {
        return mappings!!.getOrDefault(key, defaultValue)
    }

    /**
     * Registers a new value into this registry with the given key.
     *
     * @param key   the key
     * @param value the value
     * @return a new value into this registry with the given key.
     */
    fun register(key: K, value: V): V? {
        return mappings.put(key, value)
    }
}
