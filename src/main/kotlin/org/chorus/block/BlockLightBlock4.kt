package org.chorus.block

class BlockLightBlock4 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 4

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_4)

    }
}