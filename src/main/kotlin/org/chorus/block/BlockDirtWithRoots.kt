package org.chorus.block

import org.chorus.Player
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.*

/**
 * @author CoolLoong
 * @since 02.12.2022
 */
class BlockDirtWithRoots : BlockDirt {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Dirt With Roots"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 2.5

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        val vector = Vector3(
            position.x, position.y - 1,
            position.z
        )
        if (!up()!!.canBeReplaced()) {
            return false
        }
        if (item.isFertilizer && level.getBlock(vector)!!.isAir) {
            if (player != null && (player.gamemode and 0x01) == 0) {
                item.count--
            }
            level.addParticle(BoneMealParticle(this.position))
            level.setBlock(vector, get(HANGING_ROOTS))
            return true
        }
        if (item.isHoe) {
            vector.setY(position.y + 1)
            item.useOn(this)
            level.setBlock(this.position, get(DIRT), true)
            level.dropItem(vector, ItemBlock(get(HANGING_ROOTS)))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        } else if (item.isShovel) {
            item.useOn(this)
            level.setBlock(this.position, get(GRASS_PATH))
            if (player != null) {
                player.level!!.addSound(player.position, Sound.USE_GRASS)
            }
            return true
        }

        return false
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(ItemBlock(get(DIRT_WITH_ROOTS)))
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DIRT_WITH_ROOTS)

    }
}
