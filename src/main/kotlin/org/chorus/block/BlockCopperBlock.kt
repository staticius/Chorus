package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

open class BlockCopperBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBase(blockstate) {
    override val name: String
        get() = "Block of Copper"

    override val oxidizationLevel: OxidizationLevel
        get() =  OxidizationLevel.UNAFFECTED

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.COPPER_BLOCK)
    }
}