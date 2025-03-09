package org.chorus.block

class BlockLightBlock1 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 1

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_1)
            get() = Companion.field
    }
}