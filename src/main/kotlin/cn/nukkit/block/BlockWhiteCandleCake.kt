package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWhiteCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockWhiteCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}