package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockOrangeCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockOrangeCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.ORANGE_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}