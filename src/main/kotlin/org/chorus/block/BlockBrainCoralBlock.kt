package org.chorus.block

class BlockBrainCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadBrainCoralBlock()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BRAIN_CORAL_BLOCK)

    }
}