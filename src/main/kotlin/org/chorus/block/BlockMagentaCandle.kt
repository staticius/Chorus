package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMagentaCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockMagentaCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MAGENTA_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}