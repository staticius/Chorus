package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockWaxedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.UNAFFECTED

    override val isWaxed: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_COPPER_GRATE)
    }
}