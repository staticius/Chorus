package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockBlueCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockBlueCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}