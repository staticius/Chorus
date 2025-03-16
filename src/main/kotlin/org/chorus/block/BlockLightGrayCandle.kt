package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockLightGrayCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockLightGrayCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_GRAY_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}