package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockRedCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockRedCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}