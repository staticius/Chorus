package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockLightGrayCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockLightGrayCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_GRAY_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}