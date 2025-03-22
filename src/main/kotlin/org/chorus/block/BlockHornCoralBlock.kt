package org.chorus.block

class BlockHornCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadHornCoralBlock()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HORN_CORAL_BLOCK)
    }
}