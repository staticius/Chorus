package org.chorus.block

class BlockLightBlock6 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 6

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_6)

    }
}