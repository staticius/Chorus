package org.chorus.block

class BlockJungleFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Jungle Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.JUNGLE_FENCE)

    }
}