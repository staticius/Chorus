package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedAcaciaWood(blockstate: BlockState?) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_ACACIA_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}