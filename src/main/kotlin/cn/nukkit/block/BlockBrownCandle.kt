package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBrownCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockBrownCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BROWN_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}