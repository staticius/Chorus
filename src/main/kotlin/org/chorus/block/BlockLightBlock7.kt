package org.chorus.block

class BlockLightBlock7 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 7

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_7)
    }
}