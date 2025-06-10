package org.chorus_oss.chorus.block

class BlockDeadTubeCoralBlock : BlockCoralBlock {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val isDead: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_TUBE_CORAL_BLOCK)
    }
}