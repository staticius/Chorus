package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOchreFroglight @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockFroglight(blockState) {
    override val name: String
        get() = "Ochre Froglight"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OCHRE_FROGLIGHT, CommonBlockProperties.PILLAR_AXIS)

    }
}
