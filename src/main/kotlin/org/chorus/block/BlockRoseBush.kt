package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockRoseBush : BlockDoublePlant {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.ROSE

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ROSE_BUSH, CommonBlockProperties.UPPER_BLOCK_BIT)

    }
}