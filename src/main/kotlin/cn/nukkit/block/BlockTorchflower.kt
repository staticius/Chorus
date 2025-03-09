package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace

class BlockTorchflower @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlower(blockstate) {
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

    override val name: String
        get() = "Torchflower"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TORCHFLOWER)
            get() = Companion.field
    }
}