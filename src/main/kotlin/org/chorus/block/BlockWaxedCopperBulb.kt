package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockWaxedCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    override val lightLevel: Int
        get() = if (lit) 15 else 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WAXED_COPPER_BULB, CommonBlockProperties.LIT, CommonBlockProperties.POWERED_BIT)
            get() = Companion.field
    }
}