package org.chorus.block

class BlockAcaciaFence @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFence(blockstate) {
    override val name: String
        get() = "Acacia Fence"

    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_FENCE)
            get() = Companion.field
    }
}