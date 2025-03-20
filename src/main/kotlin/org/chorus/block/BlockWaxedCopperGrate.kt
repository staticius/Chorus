package org.chorus.block

class BlockWaxedCopperGrate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockCopperGrateBase(blockstate) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.UNAFFECTED


    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_COPPER_GRATE)

    }
}