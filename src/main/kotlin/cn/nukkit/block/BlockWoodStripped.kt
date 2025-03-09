package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

abstract class BlockWoodStripped(blockstate: BlockState?) : BlockWood(blockstate) {
    override fun getStrippedState(): BlockState {
        return blockState!!
    }

    override val name: String
        get() = "Stripped " + super.name

    override fun canBeActivated(): Boolean {
        return false
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }
}
