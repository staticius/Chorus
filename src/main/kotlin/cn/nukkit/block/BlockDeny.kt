package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.*
import cn.nukkit.math.*

class BlockDeny : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override val name: String
        get() = "Deny"

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (player != null && (!player.isCreative || !player.isOp)) {
            return false
        }
        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        if (player != null && (!player.isCreative || !player.isOp)) {
            return false
        }
        return super.isBreakable(vector, layer, face, item, player)
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DENY)
            get() = Companion.field
    }
}
