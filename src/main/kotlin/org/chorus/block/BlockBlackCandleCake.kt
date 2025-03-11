package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlackCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockBlackCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BLACK_CANDLE_CAKE, CommonBlockProperties.LIT)
            
    }
}