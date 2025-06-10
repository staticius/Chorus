package org.chorus_oss.chorus.block

class BlockDeadFireCoralBlock : BlockCoralBlock {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override val isDead: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties


    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_FIRE_CORAL_BLOCK)
    }
}