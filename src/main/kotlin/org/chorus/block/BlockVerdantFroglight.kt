package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockVerdantFroglight @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFroglight(blockState) {
    override val name: String
        get() = "Verdant Froglight"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.VERDANT_FROGLIGHT,
            CommonBlockProperties.PILLAR_AXIS
        )

    }
}
