package org.chorus.block

class BlockChiseledStoneBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStoneBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_STONE_BRICKS)

    }
}
