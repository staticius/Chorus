package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPurpleCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockPurpleCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}