package org.chorus.block

class BlockLightBlock3 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 3

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_3)
            get() = Companion.field
    }
}