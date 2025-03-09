package cn.nukkit.block

class BlockDeadTubeCoralBlock : BlockCoralBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(DEAD_TUBE_CORAL_BLOCK)
            get() = Companion.field
    }
}