package org.chorus.block

class BlockLightBlock8 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 8

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_8)

    }
}