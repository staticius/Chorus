package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCyanCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockCyanCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CYAN_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}