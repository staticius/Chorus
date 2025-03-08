package cn.nukkit.level.generator.terra.mappings

import java.util.function.Supplier

/**
 * A public registry holding a map of various registrations as defined by `MAPPING`.
 * The M represents the map class, which can be anything that extends [Map]. The
 * `KEY` and `VALUE` generics are the key and value respectively.
 *
 *
 * Allay Project 2023/3/18
 *
 * @param <KEY>     the key
 * @param <VALUE>   the value
 * @param <MAPPING> the map
 * @author GeyserMC | daoge_cmd
</MAPPING></VALUE></KEY> */
class SimpleMappedRegistry<KEY, VALUE, MAPPING : Map<KEY, VALUE>?> protected constructor(
    input: INPUT?,
    registryLoader: RegistryLoader<INPUT?, MAPPING>
) :
    MappedRegistry<KEY, VALUE, MAPPING> {
    override var content: MAPPING = registryLoader.load(input)

    companion object {
        /**
         * Creates a new mapped registry with the given [RegistryLoader]. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader
         * @param <INPUT></INPUT>        the input
         * @param <KEY>          the map key
         * @param <VALUE>        the map value
         * @param <MAPPING>      the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader
        </MAPPING></VALUE></KEY> */
        fun <INPUT, KEY, VALUE, MAPPING : Map<KEY, VALUE>?> of(registryLoader: RegistryLoader<INPUT?, MAPPING>): MappedRegistry<KEY, VALUE, MAPPING> {
            return SimpleMappedRegistry(null, registryLoader)
        }

        /**
         * Creates a new mapped registry with the given [RegistryLoader] and input.
         *
         * @param input          the input
         * @param registryLoader the registry loader
         * @param <INPUT></INPUT>        the input
         * @param <KEY>          the map key
         * @param <VALUE>        the map value
         * @param <MAPPING>      the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader
        </MAPPING></VALUE></KEY> */
        fun <INPUT, KEY, VALUE, MAPPING : Map<KEY, VALUE>?> of(
            input: INPUT,
            registryLoader: RegistryLoader<INPUT?, MAPPING>
        ): MappedRegistry<KEY, VALUE, MAPPING> {
            return SimpleMappedRegistry(input, registryLoader)
        }

        /**
         * Creates a new mapped registry with the given [RegistryLoader] supplier.
         * The input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader supplier
         * @param <INPUT></INPUT>        the input
         * @param <KEY>          the map key
         * @param <VALUE>        the map value
         * @param <MAPPING>      the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader supplier
        </MAPPING></VALUE></KEY> */
        fun <INPUT, KEY, VALUE, MAPPING : Map<KEY, VALUE>?> of(registryLoader: Supplier<RegistryLoader<INPUT?, MAPPING>>): MappedRegistry<KEY, VALUE, MAPPING> {
            return SimpleMappedRegistry(null, registryLoader.get())
        }

        /**
         * Creates a new mapped registry with the given [RegistryLoader] and input.
         *
         * @param registryLoader the registry loader
         * @param <INPUT></INPUT>        the input
         * @param <KEY>          the map key
         * @param <VALUE>        the map value
         * @param <MAPPING>      the returned mappings type, a map in this case
         * @return a new registry with the given RegistryLoader supplier
        </MAPPING></VALUE></KEY> */
        fun <INPUT, KEY, VALUE, MAPPING : Map<KEY, VALUE>?> of(
            input: INPUT,
            registryLoader: Supplier<RegistryLoader<INPUT?, MAPPING>>
        ): MappedRegistry<KEY, VALUE, MAPPING> {
            return SimpleMappedRegistry(input, registryLoader.get())
        }
    }
}
