package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBrownCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockBrownCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BROWN_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}