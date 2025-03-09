package cn.nukkit.block

class BlockWaxedExposedCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockExposedCopper(blockstate) {
    override val name: String
        get() = "Waxed Exposed Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_EXPOSED_COPPER)
            get() = Companion.field
    }
}