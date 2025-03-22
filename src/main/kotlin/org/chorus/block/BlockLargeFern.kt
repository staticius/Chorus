package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.DoublePlantType

class BlockLargeFern : BlockDoublePlant {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.FERN

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LARGE_FERN, CommonBlockProperties.UPPER_BLOCK_BIT)
    }
}