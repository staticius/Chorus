package org.chorus.block

class BlockLightBlock2 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 2

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_2)

    }
}