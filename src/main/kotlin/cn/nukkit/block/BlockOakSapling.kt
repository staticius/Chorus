package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockOakSapling : BlockSapling {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun getWoodType(): WoodType {
        return WoodType.OAK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OAK_SAPLING, CommonBlockProperties.AGE_BIT)
            get() = Companion.field
    }
}