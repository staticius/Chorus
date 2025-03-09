package cn.nukkit.block

class BlockCopperGrate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperGrateBase(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(COPPER_GRATE)
            get() = Companion.field
    }
}