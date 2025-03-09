package org.chorus.level.generator.terra.mappings

import java.util.function.Supplier


/**
 * An array registry that stores mappings as an array defined by [M].
 * The M represents the value that is to be stored as part of this array.
 *
 * @param <M> the mapping type
</M> */
class ArrayMappingRegistry<M>
/**
 * Creates a new array registry of this class with the given input and
 * [RegistryLoader]. The input specified is what the registry
 * loader needs to take in.
 *
 * @param input          the input
 * @param registryLoader the registry loader
 */
protected constructor(input: I?, registryLoader: RegistryLoader<I?, Array<M>>) :
    MappingRegistry<Array<M>?>(input, registryLoader) {
    /**
     * Returns the value registered by the given index.
     *
     * @param index the index
     * @return the value registered by the given index.
     */
    fun get(index: Int): M? {
        if (index >= mappings!!.size) {
            return null
        }

        return mappings!![index]
    }

    /**
     * Returns the value registered by the given index or the default value
     * specified if null.
     *
     * @param index        the index
     * @param defaultValue the default value
     * @return the value registered by the given key or the default value
     * specified if null.
     */
    fun getOrDefault(index: Int, defaultValue: M): M {
        val value = this.get(index) ?: return defaultValue

        return value
    }

    /**
     * Registers a new value into this registry with the given index.
     *
     * @param index the index
     * @param value the value
     * @return a new value into this registry with the given index.
     */
    fun register(index: Int, value: M): M {
        return value.also { mappings!![index] = it }
    }

    companion object {
        /**
         * Creates a new array registry with the given [RegistryLoader] supplier. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader supplier
         * @param <I>            the input type
         * @param <M>            the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </M></I> */
        fun <I, M> create(registryLoader: Supplier<RegistryLoader<I?, Array<M>>>): ArrayMappingRegistry<M> {
            return ArrayMappingRegistry(null, registryLoader.get())
        }

        /**
         * Creates a new array registry with the given [RegistryLoader] supplier
         * and input.
         *
         * @param input          the input
         * @param registryLoader the registry loader supplier
         * @param <I>            the input type
         * @param <M>            the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </M></I> */
        fun <I, M> create(input: I, registryLoader: Supplier<RegistryLoader<I?, Array<M>>>): ArrayMappingRegistry<M> {
            return ArrayMappingRegistry(input, registryLoader.get())
        }

        /**
         * Creates a new array registry with the given [RegistryLoader]. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader
         * @param <I>            the input type
         * @param <M>            the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </M></I> */
        fun <I, M> create(registryLoader: RegistryLoader<I?, Array<M>>): ArrayMappingRegistry<M> {
            return ArrayMappingRegistry(null, registryLoader)
        }

        /**
         * Creates a new array registry with the given [RegistryLoader] and input.
         *
         * @param input          the input
         * @param registryLoader the registry loader
         * @param <I>            the input type
         * @param <M>            the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </M></I> */
        fun <I, M> create(input: I, registryLoader: RegistryLoader<I?, Array<M>>): ArrayMappingRegistry<M> {
            return ArrayMappingRegistry(input, registryLoader)
        }
    }
}
