package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    override val lightLevel: Int
        get() = if (lit) 15 else 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(COPPER_BULB, CommonBlockProperties.LIT, CommonBlockProperties.POWERED_BIT)
            get() = Companion.field
    }
}