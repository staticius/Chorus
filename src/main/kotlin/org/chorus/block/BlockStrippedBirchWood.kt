package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedBirchWood(blockstate: BlockState) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.BIRCH
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_BIRCH_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}