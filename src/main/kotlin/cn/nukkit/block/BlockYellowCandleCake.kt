package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockYellowCandleCake @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockYellowCandle()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.YELLOW_CANDLE_CAKE, CommonBlockProperties.LIT)
            get() = Companion.field
    }
}