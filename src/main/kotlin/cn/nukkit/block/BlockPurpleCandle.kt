package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPurpleCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockPurpleCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PURPLE_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}