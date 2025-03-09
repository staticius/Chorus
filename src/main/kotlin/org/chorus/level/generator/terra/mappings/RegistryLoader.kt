package org.chorus.level.generator.terra.mappings

/**
 * Represents a registry loader. [I] is the input value, which can be anything,
 * but is commonly a file path or something similar. [O] represents the output
 * type returned by this, which can also be anything.
 *
 *
 * Allay Project 2023/3/18
 *
 * @param <I> the input to load the registry from
 * @param <O> the output of the registry
 * @author GeyserMC | daoge_cmd
</O></I> */
fun interface RegistryLoader<I, O> {
    /**
     * Loads an output from the given input.
     *
     * @param input the input
     * @return the output
     */
    fun load(input: I): O
}
