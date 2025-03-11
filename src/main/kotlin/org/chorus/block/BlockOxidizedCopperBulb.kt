package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.OxidizationLevel

class BlockOxidizedCopperBulb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBulbBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    override val lightLevel: Int
        get() = if (lit) 4 else 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OXIDIZED_COPPER_BULB, CommonBlockProperties.LIT, CommonBlockProperties.POWERED_BIT)

    }
}