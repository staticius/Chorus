package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockGreenCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockGreenCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(GREEN_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}