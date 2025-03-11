package org.chorus.block

class BlockBirchFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Birch Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BIRCH_FENCE)

    }
}