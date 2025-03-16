package org.chorus.block

class BlockDarkOakFence @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Dark Oak Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DARK_OAK_FENCE)

    }
}