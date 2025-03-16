package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedJungleWood(blockstate: BlockState) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.JUNGLE
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_JUNGLE_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}