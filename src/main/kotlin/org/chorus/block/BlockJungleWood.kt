package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockJungleWood(blockstate: BlockState) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.JUNGLE
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.JUNGLE_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}