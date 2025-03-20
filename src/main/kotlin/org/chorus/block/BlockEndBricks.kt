package org.chorus.block

class BlockEndBricks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockEndStone(blockstate) {
    override val name: String
        get() = "End Stone Bricks"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.END_BRICKS)
    }
}