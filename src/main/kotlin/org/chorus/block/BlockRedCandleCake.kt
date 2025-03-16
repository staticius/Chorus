package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockRedCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockRedCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}