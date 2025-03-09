package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedSpruceWood(blockstate: BlockState?) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.SPRUCE
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_SPRUCE_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}