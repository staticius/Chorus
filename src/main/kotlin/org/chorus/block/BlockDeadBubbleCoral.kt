package org.chorus.block

class BlockDeadBubbleCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockBubbleCoral(blockstate) {
    override fun isDead(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_BUBBLE_CORAL)

    }
}