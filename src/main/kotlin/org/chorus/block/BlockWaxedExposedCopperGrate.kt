package org.chorus.block

class BlockWaxedExposedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.EXPOSED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_EXPOSED_COPPER_GRATE)

    }
}