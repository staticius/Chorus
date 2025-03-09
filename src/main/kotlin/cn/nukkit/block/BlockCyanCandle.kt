package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCyanCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockCyanCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CYAN_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}