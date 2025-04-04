package org.chorus.block

class BlockWaxedExposedChiseledCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_EXPOSED_CHISELED_COPPER)

    }
}