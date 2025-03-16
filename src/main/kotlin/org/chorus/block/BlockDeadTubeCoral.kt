package org.chorus.block

class BlockDeadTubeCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTubeCoral(blockstate) {
    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_TUBE_CORAL)

    }
}