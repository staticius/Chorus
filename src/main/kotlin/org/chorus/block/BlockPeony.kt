package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.DoublePlantType

class BlockPeony : BlockDoublePlant {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.PAEONIA

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PEONY, CommonBlockProperties.UPPER_BLOCK_BIT)
    }
}