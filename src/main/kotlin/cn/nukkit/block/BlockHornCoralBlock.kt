package cn.nukkit.block

class BlockHornCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override fun toDead(): BlockCoralBlock {
        return BlockDeadHornCoralBlock()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(HORN_CORAL_BLOCK)
            get() = Companion.field
    }
}