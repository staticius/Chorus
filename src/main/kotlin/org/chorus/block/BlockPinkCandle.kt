package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPinkCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockPinkCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PINK_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}