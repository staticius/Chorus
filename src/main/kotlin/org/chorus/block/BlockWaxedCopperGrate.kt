package org.chorus.block

class BlockWaxedCopperGrate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_COPPER_GRATE)

    }
}