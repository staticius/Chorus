package cn.nukkit.block

import cn.nukkit.item.*

/**
 * @author Nukkit Project Team
 */
class BlockClay : BlockSolid, Natural {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override val name: String
        get() = "Clay Block"

    override fun getDrops(item: Item): Array<Item?>? {
        val clayBall: Item = ItemClayBall()
        clayBall.setCount(4)
        return arrayOf(
            clayBall
        )
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CLAY)
            get() = Companion.field
    }
}
