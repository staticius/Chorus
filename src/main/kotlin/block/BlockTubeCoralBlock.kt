package org.chorus_oss.chorus.block

class BlockTubeCoralBlock : BlockCoralBlock {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadTubeCoralBlock()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TUBE_CORAL_BLOCK)
    }
}