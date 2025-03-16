package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedAcaciaWood(blockstate: BlockState) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_ACACIA_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}