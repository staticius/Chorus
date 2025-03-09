package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockYellowCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockYellowCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.YELLOW_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}