package org.chorus.block

class BlockWeatheredChiseledCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WEATHERED_CHISELED_COPPER)

    }
}