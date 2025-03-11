package org.chorus.block

class BlockWeatheredCopperGrate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WEATHERED_COPPER_GRATE)
            
    }
}