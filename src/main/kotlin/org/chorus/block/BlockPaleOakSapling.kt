package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockPaleOakSapling : BlockSapling {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun getWoodType(): WoodType {
        return WoodType.PALE_OAK
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_OAK_SAPLING, CommonBlockProperties.AGE_BIT)
            get() = Companion.field
    }
}