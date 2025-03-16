package org.chorus.block

class BlockCrackedStoneBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStoneBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRACKED_STONE_BRICKS)

    }
}
