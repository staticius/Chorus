package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockGrayCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockGrayCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(GRAY_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}