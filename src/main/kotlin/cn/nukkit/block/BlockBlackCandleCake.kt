package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBlackCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockBlackCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLACK_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}