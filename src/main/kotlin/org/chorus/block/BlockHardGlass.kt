package org.chorus.block

class BlockHardGlass @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GLASS)

    }
}