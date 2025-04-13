package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockYellowCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockYellowCandleCake()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.YELLOW_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}