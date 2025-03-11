package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockExposedChiseledCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.EXPOSED_CHISELED_COPPER)

    }
}