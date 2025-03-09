package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockMagentaCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockMagentaCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}