package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockOxidizedChiseledCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_CHISELED_COPPER)

    }
}