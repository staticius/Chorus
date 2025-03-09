package cn.nukkit.block

class BlockBubbleCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadBubbleCoralBlock()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BUBBLE_CORAL_BLOCK)
            get() = Companion.field
    }
}