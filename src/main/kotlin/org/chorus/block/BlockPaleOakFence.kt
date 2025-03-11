package org.chorus.block

class BlockPaleOakFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Pale Oak Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_OAK_FENCE)

    }
}