package org.chorus.block

class BlockChiseledStoneBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStoneBricks(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_STONE_BRICKS)
    }
}
