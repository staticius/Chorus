package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCyanCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockCyanCandle()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CYAN_CANDLE_CAKE, CommonBlockProperties.LIT)
    }
}