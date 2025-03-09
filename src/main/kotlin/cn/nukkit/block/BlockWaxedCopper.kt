package cn.nukkit.block

class BlockWaxedCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCopperBlock(blockstate) {
    override val name: String
        get() = "Waxed Block of Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_COPPER)
            get() = Companion.field
    }
}