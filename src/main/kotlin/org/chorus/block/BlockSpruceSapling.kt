package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockSpruceSapling : BlockSapling {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState) :  super(blockstate)

    override val woodType: WoodType
        get() = WoodType.SPRUCE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_SAPLING, CommonBlockProperties.AGE_BIT)

    }
}