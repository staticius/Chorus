package org.chorus.block

class BlockMangroveFence @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Mangrove Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MANGROVE_FENCE)

    }
}