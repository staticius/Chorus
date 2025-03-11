package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWeatheredCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    override val lightLevel: Int
        get() = if (lit) 8 else 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WEATHERED_COPPER_BULB, CommonBlockProperties.LIT, CommonBlockProperties.POWERED_BIT)

    }
}