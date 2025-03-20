package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockExposedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.EXPOSED

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.EXPOSED_COPPER_GRATE)
    }
}