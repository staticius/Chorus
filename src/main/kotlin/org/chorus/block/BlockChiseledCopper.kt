package org.chorus.block

class BlockChiseledCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_COPPER)

    }
}