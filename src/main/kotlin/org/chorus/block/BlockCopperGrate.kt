package org.chorus.block

class BlockCopperGrate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.COPPER_GRATE)

    }
}