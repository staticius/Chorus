package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBlueCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockBlueCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BLUE_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}