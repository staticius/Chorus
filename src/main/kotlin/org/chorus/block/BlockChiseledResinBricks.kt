package org.chorus.block

class BlockChiseledResinBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockResinBricks(blockstate) {
        
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_RESIN_BRICKS)
    }
}
