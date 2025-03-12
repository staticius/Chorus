package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockWaxedOxidizedCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    override val lightLevel: Int
        get() = if (lit) 4 else 0

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.WAXED_OXIDIZED_COPPER_BULB,
            CommonBlockProperties.LIT,
            CommonBlockProperties.POWERED_BIT
        )

    }
}