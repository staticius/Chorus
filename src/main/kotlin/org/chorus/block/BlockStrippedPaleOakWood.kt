package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedPaleOakWood(blockstate: BlockState?) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.PALE_OAK
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_PALE_OAK_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}