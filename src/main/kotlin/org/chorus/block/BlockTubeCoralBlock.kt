package org.chorus.block

class BlockTubeCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadTubeCoralBlock()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TUBE_CORAL_BLOCK)

    }
}