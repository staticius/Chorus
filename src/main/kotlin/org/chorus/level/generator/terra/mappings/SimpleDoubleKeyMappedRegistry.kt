package org.chorus.level.generator.terra.mappings

import org.chorus.level.generator.terra.mappings.DoubleKeyMappedRegistry.MapPair

/**
 * Allay Project 2023/10/27
 *
 * @author daoge_cmd
 */
class SimpleDoubleKeyMappedRegistry<K1, K2, VALUE> protected constructor(
    input: INPUT?,
    registryLoader: RegistryLoader<INPUT?, MapPair<K1, K2, VALUE>>
) :
    DoubleKeyMappedRegistry<K1, K2, VALUE> {
    override var content: MapPair<K1, K2, VALUE> = registryLoader.load(input)
}
