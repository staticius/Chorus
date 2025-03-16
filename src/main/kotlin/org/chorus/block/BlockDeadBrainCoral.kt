package org.chorus.block

class BlockDeadBrainCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockBrainCoral(blockstate) {
    override fun isDead(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_BRAIN_CORAL)

    }
}