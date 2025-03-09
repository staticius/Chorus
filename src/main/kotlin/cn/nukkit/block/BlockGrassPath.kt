package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.level.Sound
import cn.nukkit.math.BlockFace

/**
 * @author xtypr
 * @since 2015/11/22
 */
class BlockGrassPath : BlockGrassBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Dirt Path"

    override val hardness: Double
        get() = 0.65

    override val resistance: Double
        get() = 0.65

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (up()!!.isSolid) {
                level.setBlock(this.position, get(DIRT), false, true)
            }

            return Level.BLOCK_UPDATE_NORMAL
        }
        return 0
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isHoe) {
            item.useOn(this)
            level.setBlock(this.position, get(FARMLAND), true)
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }

        return false
    }

    override val isTransparent: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(GRASS_PATH)
            get() = Companion.field
    }
}
