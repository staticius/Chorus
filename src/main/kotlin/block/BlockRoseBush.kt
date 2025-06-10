package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.block.property.enums.DoublePlantType

class BlockRoseBush : BlockDoublePlant {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.ROSE

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ROSE_BUSH, CommonBlockProperties.UPPER_BLOCK_BIT)
    }
}