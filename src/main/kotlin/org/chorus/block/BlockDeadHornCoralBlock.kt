package org.chorus.block

class BlockDeadHornCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_HORN_CORAL_BLOCK)

    }
}