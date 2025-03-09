package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlackCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockBlackCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BLACK_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}