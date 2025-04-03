package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockPaleOakSapling : BlockSapling {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override fun getWoodType(): WoodType {
        return WoodType.PALE_OAK
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_OAK_SAPLING, CommonBlockProperties.AGE_BIT)
    }
}