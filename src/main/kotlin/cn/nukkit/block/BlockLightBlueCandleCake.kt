package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockLightBlueCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockLightBlueCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLUE_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}