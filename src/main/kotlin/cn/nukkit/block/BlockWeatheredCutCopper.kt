package cn.nukkit.block

open class BlockWeatheredCutCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCutCopper(blockstate) {
    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.WEATHERED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WEATHERED_CUT_COPPER)
            get() = Companion.field
    }
}