package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

open class BlockExposedCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBlock(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.EXPOSED_COPPER)

    }
}