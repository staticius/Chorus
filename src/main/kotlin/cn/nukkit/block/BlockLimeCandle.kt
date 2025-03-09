package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockLimeCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockLimeCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIME_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}