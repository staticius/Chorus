package org.chorus_oss.chorus.block

class BlockFireCoralBlock : BlockCoralBlock {
    constructor() : super(properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadFireCoralBlock()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.FIRE_CORAL_BLOCK)
    }
}