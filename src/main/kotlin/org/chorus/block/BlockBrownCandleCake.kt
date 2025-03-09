package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBrownCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockBrownCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BROWN_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}