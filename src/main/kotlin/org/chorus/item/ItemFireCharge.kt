package org.chorus.item

import org.chorus.Player
import org.chorus.block.*
import org.chorus.event.block.BlockIgniteEvent
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.network.protocol.LevelEventPacket
import java.util.concurrent.ThreadLocalRandom

/**
 * @author PetteriM1
 */
class ItemFireCharge @JvmOverloads constructor(meta: Int? = 0, count: Int = 1) :
    Item(ItemID.Companion.FIRE_CHARGE, 0, count, "Fire Charge") {
    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        level: Level,
        player: Player,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        if (player.isAdventure) {
            return false
        }

        if (block.id == BlockID.AIR && target.burnChance != -1 && (target.isSolid || target.burnChance > 0)) {
            if (target.id == BlockID.OBSIDIAN) {
                if (level.createPortal(target)) {
                    return true
                }
            }

            val fire = Block.get(BlockID.FIRE) as BlockFire
            fire.position.x = block.position.x
            fire.position.y = block.position.y
            fire.position.z = block.position.z
            fire.level = level

            if (fire.isBlockTopFacingSurfaceSolid(fire.down()) || fire.canNeighborBurn()) {
                val e = BlockIgniteEvent(block, null, player, BlockIgniteEvent.BlockIgniteCause.FLINT_AND_STEEL)
                block.level.server.pluginManager.callEvent(e)

                if (!e.isCancelled) {
                    level.setBlock(fire.position, fire, true)
                    level.addLevelEvent(block.position, LevelEventPacket.EVENT_SOUND_GHAST_FIREBALL, 78642)
                    level.scheduleUpdate(fire, fire.tickRate() + ThreadLocalRandom.current().nextInt(10))
                }
                if (player.isSurvival) {
                    val item = player.getInventory().itemInHand
                    item.setCount(item.getCount() - 1)
                    player.getInventory().setItemInHand(item)
                }
                return true
            }
        }
        return false
    }
}
