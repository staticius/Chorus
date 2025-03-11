package org.chorus.block

class BlockWaxedWeatheredChiseledCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_WEATHERED_CHISELED_COPPER)

    }
}