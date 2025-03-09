package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.DoublePlantType

class BlockLilac : BlockDoublePlant {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.SYRINGA

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LILAC, CommonBlockProperties.UPPER_BLOCK_BIT)
            get() = Companion.field
    }
}