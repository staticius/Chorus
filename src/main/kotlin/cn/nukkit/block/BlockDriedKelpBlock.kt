package cn.nukkit.block


class BlockDriedKelpBlock : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Dried Kelp Block"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 2.5

    companion object {
        val properties: BlockProperties = BlockProperties(DRIED_KELP_BLOCK)
            get() = Companion.field
    }
}
