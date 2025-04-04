package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockYellowCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockYellowCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}