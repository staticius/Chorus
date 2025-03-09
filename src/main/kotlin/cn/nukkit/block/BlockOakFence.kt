package cn.nukkit.block

class BlockOakFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Oak Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OAK_FENCE)
            get() = Companion.field
    }
}