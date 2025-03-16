package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockStrippedDarkOakWood(blockstate: BlockState) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.DARK_OAK
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_DARK_OAK_WOOD, CommonBlockProperties.PILLAR_AXIS)

    }
}