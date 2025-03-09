package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockGrayCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockGrayCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(GRAY_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}