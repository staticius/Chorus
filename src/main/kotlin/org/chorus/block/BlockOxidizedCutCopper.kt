package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

open class BlockOxidizedCutCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCutCopper(blockstate) {
    override val name: String
        get() = "Cut Oxidized Copper"

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_CUT_COPPER)

    }
}