package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockSunflower : BlockDoublePlant {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState?) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.SUNFLOWER

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SUNFLOWER, CommonBlockProperties.UPPER_BLOCK_BIT)
            
    }
}