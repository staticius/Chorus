package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.DoublePlantType

class BlockLargeFern : BlockDoublePlant {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.FERN

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LARGE_FERN, CommonBlockProperties.UPPER_BLOCK_BIT)
            get() = Companion.field
    }
}