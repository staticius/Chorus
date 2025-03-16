package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockLightBlueCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockLightBlueCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_BLUE_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}