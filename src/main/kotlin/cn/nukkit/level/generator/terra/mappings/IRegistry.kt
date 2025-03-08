package cn.nukkit.level.generator.terra.mappings

import java.util.function.Consumer

/**
 * Represents a registry.
 *
 * @param <M> the value being held by the registry
</M> */
internal interface IRegistry<M> {
    /**
     * Gets the underlying value held by this registry.
     *
     * @return the underlying value held by this registry.
     */
    fun get(): M

    /**
     * Sets the underlying value held by this registry.
     * Clears any existing data associated with the previous
     * value.
     *
     * @param mappings the underlying value held by this registry
     */
    fun set(mappings: M)

    /**
     * Registers what is specified in the given
     * [Consumer] into the underlying value.
     *
     * @param consumer the consumer
     */
    fun register(consumer: Consumer<M>)
}
