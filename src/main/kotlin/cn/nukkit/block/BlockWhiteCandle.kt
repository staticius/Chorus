package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWhiteCandle @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCandle(blockstate) {
    override fun toCakeForm(): Block {
        return BlockWhiteCandleCake()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WHITE_CANDLE, CommonBlockProperties.CANDLES, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}