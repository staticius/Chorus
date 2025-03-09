package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedBirchWood(blockstate: BlockState?) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.BIRCH
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_BIRCH_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}