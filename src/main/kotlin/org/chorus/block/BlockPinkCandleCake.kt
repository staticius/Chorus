package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPinkCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockPinkCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PINK_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}