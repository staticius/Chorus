package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockGrayCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockGrayCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRAY_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}