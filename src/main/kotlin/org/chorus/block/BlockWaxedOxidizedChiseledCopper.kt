package org.chorus.block

class BlockWaxedOxidizedChiseledCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_OXIDIZED_CHISELED_COPPER)

    }
}