package org.chorus.block

class BlockWaxedChiseledCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.UNAFFECTED


    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_CHISELED_COPPER)

    }
}