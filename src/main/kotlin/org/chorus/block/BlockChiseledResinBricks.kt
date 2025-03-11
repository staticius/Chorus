package org.chorus.block

class BlockChiseledResinBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockResinBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_RESIN_BRICKS)

    }
}
