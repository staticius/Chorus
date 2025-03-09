package org.chorus.block

class BlockLightBlock11 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 11

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_11)
            get() = Companion.field
    }
}