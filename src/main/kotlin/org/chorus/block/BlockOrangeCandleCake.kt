package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockOrangeCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockOrangeCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}