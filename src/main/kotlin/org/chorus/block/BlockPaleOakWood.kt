package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockPaleOakWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.PALE_OAK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_OAK_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}