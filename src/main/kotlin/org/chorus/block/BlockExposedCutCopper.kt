package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

open class BlockExposedCutCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCutCopper(blockstate) {
    override val name: String
        get() = "Exposed Cut Copper"

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.EXPOSED_CUT_COPPER)

    }
}