package org.chorus.block


class BlockBambooFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Bamboo Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BAMBOO_FENCE)

    }
}