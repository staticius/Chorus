package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockStrippedOakWood(blockstate: BlockState?) : BlockWoodStripped(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.OAK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.STRIPPED_OAK_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}