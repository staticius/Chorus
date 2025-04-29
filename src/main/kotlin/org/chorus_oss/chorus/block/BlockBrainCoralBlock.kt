package org.chorus_oss.chorus.block

class BlockBrainCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadBrainCoralBlock()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BRAIN_CORAL_BLOCK)
    }
}