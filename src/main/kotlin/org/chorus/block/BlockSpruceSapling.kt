package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockSpruceSapling : BlockSapling {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState?) : super(blockstate)

    override val woodType: WoodType?
        get() = WoodType.SPRUCE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_SAPLING, CommonBlockProperties.AGE_BIT)
            get() = Companion.field
    }
}