package cn.nukkit.level.generator.terra.mappings

import java.util.function.Supplier


/**
 * An public registry holding a map of various registrations as defined by [M].
 * The M represents the map class, which can be anything that extends [Map]. The
 * [K] and [V] generics are the key and value respectively.
 *
 * @param <K> the key
 * @param <V> the value
 * @param <M> the map
</M></V></K> */
class MappedMappingRegistry<K, V, M : Map<K, V>?> protected constructor(
    input: I?,
    registryLoader: RegistryLoader<I?, M>
) :
    AbstractMappedRegistry<K, V, M>(input, registryLoader) {
    companion object {
        /**
         * Creates a new mapped registry with the given [RegistryLoader]. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <K> the map key
         * @param <V> the map value
         * @param <M> the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader
        </M></V></K></I> */
        fun <I, K, V, M : Map<K, V>?> create(registryLoader: RegistryLoader<I?, M>): MappedMappingRegistry<K, V, M> {
            return MappedMappingRegistry(null, registryLoader)
        }

        /**
         * Creates a new mapped registry with the given [RegistryLoader] and input.
         *
         * @param input the input
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <K> the map key
         * @param <V> the map value
         * @param <M> the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader
        </M></V></K></I> */
        fun <I, K, V, M : Map<K, V>?> create(
            input: I,
            registryLoader: RegistryLoader<I?, M>
        ): MappedMappingRegistry<K, V, M> {
            return MappedMappingRegistry(input, registryLoader)
        }

        /**
         * Creates a new mapped registry with the given [RegistryLoader] supplier.
         * The input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader supplier
         * @param <I> the input
         * @param <K> the map key
         * @param <V> the map value
         * @param <M> the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader supplier
        </M></V></K></I> */
        fun <I, K, V, M : Map<K, V>?> create(registryLoader: Supplier<RegistryLoader<I?, M>>): MappedMappingRegistry<K, V, M> {
            return MappedMappingRegistry(null, registryLoader.get())
        }

        /**
         * Creates a new mapped registry with the given [RegistryLoader] and input.
         *
         * @param registryLoader the registry loader
         * @param <I> the input
         * @param <K> the map key
         * @param <V> the map value
         * @param <M> the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader supplier
        </M></V></K></I> */
        fun <I, K, V, M : Map<K, V>?> create(
            input: I,
            registryLoader: Supplier<RegistryLoader<I?, M>>
        ): MappedMappingRegistry<K, V, M> {
            return MappedMappingRegistry(input, registryLoader.get())
        }
    }
}
