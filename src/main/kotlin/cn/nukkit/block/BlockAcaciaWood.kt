package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockAcaciaWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}