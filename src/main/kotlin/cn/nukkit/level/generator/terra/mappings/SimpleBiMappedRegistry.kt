package cn.nukkit.level.generator.terra.mappings

import com.google.common.collect.BiMap

/**
 * Allay Project 2023/10/28
 *
 * @author daoge_cmd
 */
class SimpleBiMappedRegistry<LEFT, RIGHT> protected constructor(
    input: INPUT?,
    registryLoader: RegistryLoader<INPUT?, BiMap<LEFT, RIGHT>>
) :
    BiMappedRegistry<LEFT, RIGHT> {
    override var content: BiMap<LEFT, RIGHT> = registryLoader.load(input)

    companion object {
        fun <INPUT, LEFT, RIGHT> of(registryLoader: RegistryLoader<INPUT?, BiMap<LEFT, RIGHT>>): SimpleBiMappedRegistry<LEFT, RIGHT> {
            return SimpleBiMappedRegistry(null, registryLoader)
        }
    }
}
