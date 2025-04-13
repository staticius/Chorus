package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockMagentaCandleCake @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCandleCake(blockstate) {
    override fun toCandleForm(): BlockCandle {
        return BlockMagentaCandle()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MAGENTA_CANDLE_CAKE, CommonBlockProperties.LIT)

    }
}