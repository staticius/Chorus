package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.math.BlockFace

/**
 * @author xtypr
 * @since 2015/11/22
 */
class BlockPodzol : BlockDirt {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Podzol"

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (!up()!!.canBeReplaced()) {
            return false
        }

        if (item.isShovel) {
            item.useOn(this)
            level.setBlock(this.position, get(BlockID.GRASS_PATH))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PODZOL)
            get() = Companion.field
    }
}
