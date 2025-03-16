package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockGreenCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockGreenCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GREEN_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}