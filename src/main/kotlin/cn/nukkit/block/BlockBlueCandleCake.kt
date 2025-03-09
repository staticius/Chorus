package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockBlueCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockBlueCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}