package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockOxidizedCopperGrate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_COPPER_GRATE)
            get() = Companion.field
    }
}