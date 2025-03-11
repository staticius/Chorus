package org.chorus.block

class BlockDeadTubeCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_TUBE_CORAL_BLOCK)

    }
}