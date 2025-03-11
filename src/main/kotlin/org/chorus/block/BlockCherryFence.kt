package org.chorus.block

class BlockCherryFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Cherry Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHERRY_FENCE)

    }
}