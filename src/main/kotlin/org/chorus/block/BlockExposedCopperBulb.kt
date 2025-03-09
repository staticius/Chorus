package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.OxidizationLevel

class BlockExposedCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    override val lightLevel: Int
        get() = if (lit) 12 else 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(EXPOSED_COPPER_BULB, CommonBlockProperties.LIT, CommonBlockProperties.POWERED_BIT)
            get() = Companion.field
    }
}