package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPinkCandle @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockPinkCandleCake()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PINK_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)

    }
}