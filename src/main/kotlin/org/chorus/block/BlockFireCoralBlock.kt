package org.chorus.block

class BlockFireCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadFireCoralBlock()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(FIRE_CORAL_BLOCK)
            get() = Companion.field
    }
}