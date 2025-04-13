package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockGrayCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockGrayCandleCake()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.GRAY_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
    }
}