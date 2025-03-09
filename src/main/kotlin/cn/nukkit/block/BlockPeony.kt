package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.DoublePlantType

class BlockPeony : BlockDoublePlant {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val doublePlantType: DoublePlantType
        get() = DoublePlantType.PAEONIA

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PEONY, CommonBlockProperties.UPPER_BLOCK_BIT)
            get() = Companion.field
    }
}