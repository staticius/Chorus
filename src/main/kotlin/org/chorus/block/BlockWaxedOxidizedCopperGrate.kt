package org.chorus.block

class BlockWaxedOxidizedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_OXIDIZED_COPPER_GRATE)

    }
}