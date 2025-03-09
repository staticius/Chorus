package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType

class BlockAcaciaSapling : BlockSapling {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun getWoodType(): WoodType {
        return WoodType.ACACIA
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_SAPLING, CommonBlockProperties.AGE_BIT)
            get() = Companion.field
    }
}