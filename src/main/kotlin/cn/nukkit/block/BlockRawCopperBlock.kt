package cn.nukkit.block

class BlockRawCopperBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockRaw(blockstate) {
    override val name: String
        get() = "Block of Raw Copper"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RAW_COPPER_BLOCK)
            get() = Companion.field
    }
}