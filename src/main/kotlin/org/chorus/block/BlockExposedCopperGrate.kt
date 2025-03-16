package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockExposedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.EXPOSED_COPPER_GRATE)

    }
}