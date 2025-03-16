package org.chorus.block

class BlockWaxedWeatheredCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_WEATHERED_COPPER_GRATE)

    }
}