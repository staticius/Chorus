package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.level.Level
import org.chorus.level.Sound
import org.chorus.math.BlockFace

class BlockGrassPath : BlockGrassBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Dirt Path"

    override val hardness: Double
        get() = 0.65

    override val resistance: Double
        get() = 0.65

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (up().isSolid) {
                level.setBlock(this.position, get(BlockID.DIRT), false, true)
            }

            return Level.BLOCK_UPDATE_NORMAL
        }
        return 0
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isHoe) {
            item.useOn(this)
            level.setBlock(this.position, get(BlockID.FARMLAND), true)
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }

        return false
    }

    override val isTransparent: Boolean
        get() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GRASS_PATH)
    }
}
