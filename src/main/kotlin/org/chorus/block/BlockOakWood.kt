package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockOakWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.OAK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OAK_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}