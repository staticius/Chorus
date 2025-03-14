package org.chorus.block

class BlockDeadBubbleCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_BUBBLE_CORAL_BLOCK)

    }
}