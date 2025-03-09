package org.chorus.level.generator.terra.mappings

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import java.util.function.Supplier

/**
 * A versioned, mapped registry. Like [SimpleMappedRegistry], the [Map] interface is
 * not able to be specified here, but unlike it, it does not have support for specialized
 * instances, and ONLY supports [Int2ObjectMap] for optimal performance to prevent boxing
 * of integers.
 *
 * @param <V> the value
</V> */
class VersionedMappingRegistry<V> protected constructor(
    input: I?,
    registryLoader: RegistryLoader<I?, Int2ObjectMap<V>>
) :
    AbstractMappedRegistry<Int?, V, Int2ObjectMap<V>?>(input, registryLoader) {
    /**
     * Gets the closest value for the specified version. Only
     * returns versions higher up than the specified if one
     * does not exist for the given one. Useful in the event
     * that you want to get a resource which is guaranteed for
     * older versions, but not on newer ones.
     *
     * @param version the version
     * @return the closest value for the specified version
     */
    fun forVersion(version: Int): V? {
        var current: Int2ObjectMap.Entry<V>? = null
        for (entry in mappings!!.int2ObjectEntrySet()) {
            val currentVersion = entry.intKey
            if (version < currentVersion) {
                continue
            }
            if (version == currentVersion) {
                return entry.getValue()
            }
            if (current == null || current.intKey < currentVersion) {
                // This version is newer and should be prioritized
                current = entry
            }
        }
        return current?.getValue()
    }

    companion object {
        /**
         * Creates a new versioned registry with the given [RegistryLoader]. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <V> the map value
         * @return a new registry with the given RegistryLoader
        </V></I> */
        fun <I, V> create(registryLoader: RegistryLoader<I?, Int2ObjectMap<V>>): VersionedMappingRegistry<V> {
            return VersionedMappingRegistry(null, registryLoader)
        }

        /**
         * Creates a new versioned registry with the given [RegistryLoader] and input.
         *
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <V> the map value
         * @return a new registry with the given RegistryLoader
        </V></I> */
        fun <I, V> create(input: I, registryLoader: RegistryLoader<I?, Int2ObjectMap<V>>): VersionedMappingRegistry<V> {
            return VersionedMappingRegistry(input, registryLoader)
        }

        /**
         * Creates a new versioned registry with the given [RegistryLoader] supplier.
         * The input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <V> the map value
         * @return a new registry with the given RegistryLoader supplier
        </V></I> */
        fun <I, V> create(registryLoader: Supplier<RegistryLoader<I?, Int2ObjectMap<V>>>): VersionedMappingRegistry<V> {
            return VersionedMappingRegistry(null, registryLoader.get())
        }

        /**
         * Creates a new versioned registry with the given [RegistryLoader] supplier and input.
         *
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <V> the map value
         * @return a new registry with the given RegistryLoader supplier
        </V></I> */
        fun <I, V> create(
            input: I,
            registryLoader: Supplier<RegistryLoader<I?, Int2ObjectMap<V>>>
        ): VersionedMappingRegistry<V> {
            return VersionedMappingRegistry(input, registryLoader.get())
        }
    }
}
