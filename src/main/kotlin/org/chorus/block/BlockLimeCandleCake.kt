package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockLimeCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockLimeCandle()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_CANDLE_CAKE, CommonBlockProperties.LIT)
    }
}