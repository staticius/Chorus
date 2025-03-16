package org.chorus.block

class BlockAcaciaFence @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Acacia Fence"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ACACIA_FENCE)

    }
}