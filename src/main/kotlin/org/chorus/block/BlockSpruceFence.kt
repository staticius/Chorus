package org.chorus.block

class BlockSpruceFence @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Spruce Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_FENCE)

    }
}