package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockSpruceWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.SPRUCE
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}