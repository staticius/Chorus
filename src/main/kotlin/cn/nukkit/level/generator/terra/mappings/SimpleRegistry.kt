package cn.nukkit.level.generator.terra.mappings

import java.util.function.Supplier

/**
 * A simple registry with no defined mapping or input type. Designed to allow
 * for simple registrations of any given type without restrictions on what
 * the input or output can be.
 *
 *
 * Allay Project 2023/3/18
 *
 * @param <CONTENT> the value being held by the registry
 * @author GeyserMC | daoge_cmd
</CONTENT> */
class SimpleRegistry<CONTENT> protected constructor(input: INPUT?, registryLoader: RegistryLoader<INPUT?, CONTENT>) :
    Registry<CONTENT> {
    override var content: CONTENT = registryLoader.load(input)

    companion object {
        /**
         * Creates a new registry with the given [RegistryLoader] supplier. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader supplier
         * @param <INPUT></INPUT>        the input type
         * @param <CONTENT>      the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </CONTENT> */
        fun <INPUT, CONTENT> of(registryLoader: Supplier<RegistryLoader<INPUT?, CONTENT>>): SimpleRegistry<CONTENT> {
            return SimpleRegistry(null, registryLoader.get())
        }

        /**
         * Creates a new registry with the given [RegistryLoader] supplier
         * and input.
         *
         * @param input          the input
         * @param registryLoader the registry loader supplier
         * @param <INPUT></INPUT>        the input type
         * @param <CONTENT>      the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </CONTENT> */
        fun <INPUT, CONTENT> of(
            input: INPUT,
            registryLoader: Supplier<RegistryLoader<INPUT?, CONTENT>>
        ): SimpleRegistry<CONTENT> {
            return SimpleRegistry(input, registryLoader.get())
        }

        /**
         * Creates a new registry with the given [RegistryLoader]. The
         * input type is not specified here, meaning the loader return type is either
         * predefined, or the registry is populated at a later point.
         *
         * @param registryLoader the registry loader
         * @param <INPUT></INPUT>        the input type
         * @param <CONTENT>      the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </CONTENT> */
        fun <INPUT, CONTENT> of(registryLoader: RegistryLoader<INPUT?, CONTENT>): SimpleRegistry<CONTENT> {
            return SimpleRegistry(null, registryLoader)
        }

        /**
         * Creates a new registry with the given [RegistryLoader] and input.
         *
         * @param input          the input
         * @param registryLoader the registry loader
         * @param <INPUT></INPUT>        the input type
         * @param <CONTENT>      the returned mappings type
         * @return a new registry with the given RegistryLoader supplier
        </CONTENT> */
        fun <INPUT, CONTENT> of(
            input: INPUT,
            registryLoader: RegistryLoader<INPUT?, CONTENT>
        ): SimpleRegistry<CONTENT> {
            return SimpleRegistry(input, registryLoader)
        }
    }
}
