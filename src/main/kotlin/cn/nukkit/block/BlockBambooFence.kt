package cn.nukkit.block


class BlockBambooFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Bamboo Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BAMBOO_FENCE)
            get() = Companion.field
    }
}