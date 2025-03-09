package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockLimeCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockLimeCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIME_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}