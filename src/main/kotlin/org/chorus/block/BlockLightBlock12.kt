package org.chorus.block

class BlockLightBlock12 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 12

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_12)
    }
}