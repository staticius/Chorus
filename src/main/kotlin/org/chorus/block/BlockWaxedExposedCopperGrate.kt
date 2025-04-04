package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockWaxedExposedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.EXPOSED
        }

    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_EXPOSED_COPPER_GRATE)
    }
}