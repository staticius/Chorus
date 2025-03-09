package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

class BlockHangingRoots @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHanging(blockstate) {
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
        return isSupportValid && super.place(item, block, target, face, fx, fy, fz, player)
    }

    override val isSupportValid: Boolean
        get() = up()!!.isSolid

    companion object {
        val properties: BlockProperties = BlockProperties(HANGING_ROOTS)
            get() = Companion.field
    }
}