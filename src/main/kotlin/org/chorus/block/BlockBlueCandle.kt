package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlueCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockBlueCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BLUE_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}