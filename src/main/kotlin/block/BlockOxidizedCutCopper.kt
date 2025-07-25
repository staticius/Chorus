package org.chorus_oss.chorus.block

import org.chorus_oss.chorus.block.property.enums.OxidizationLevel

open class BlockOxidizedCutCopper @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockCutCopper(blockstate) {
    override val name: String
        get() = "Cut Oxidized Copper"

    override val oxidizationLevel
        get(): OxidizationLevel {
            return OxidizationLevel.OXIDIZED
        }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_CUT_COPPER)
    }
}