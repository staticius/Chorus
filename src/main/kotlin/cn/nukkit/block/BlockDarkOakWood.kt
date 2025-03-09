package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockDarkOakWood(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getWoodType(): WoodType {
        return WoodType.DARK_OAK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DARK_OAK_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}