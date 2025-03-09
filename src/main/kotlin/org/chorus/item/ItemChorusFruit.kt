package org.chorus.item

import org.chorus.Player
import org.chorus.block.BlockLiquid
import org.chorus.event.player.PlayerTeleportEvent
import org.chorus.math.*
import org.chorus.network.protocol.LevelSoundEventPacket
import org.chorus.utils.random.NukkitRandom

/**
 * @author Leonidius20
 * @since 20.08.18
 */
class ItemChorusFruit @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.CHORUS_FRUIT, meta, count, "Chorus Fruit") {
    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return player.isItemCoolDownEnd(this.getIdentifier())
    }

    override fun afterUse(player: Player) {
        player.setItemCoolDown(20, this.getIdentifier()) //ban 20tick for the item
    }

    override fun onEaten(player: Player): Boolean {
        val minX = player.position.floorX - 8
        val minY = player.position.floorY - 8
        val minZ = player.position.floorZ - 8
        val maxX = minX + 16
        val maxY = minY + 16
        val maxZ = minZ + 16

        val level = player.level ?: return false
        if (player.isInsideOfWater()) return false

        val random = NukkitRandom()
        for (attempts in 0..127) {
            val x = random.nextInt(minX, maxX)
            var y = random.nextInt(minY, maxY)
            val z = random.nextInt(minZ, maxZ)

            if (y < 0) continue

            while (y >= 0 && !level.getBlock(Vector3(x.toDouble(), (y + 1).toDouble(), z.toDouble())).isSolid) {
                y--
            }
            y++ // Back up to non solid

            val blockUp = level.getBlock(Vector3(x.toDouble(), (y + 1).toDouble(), z.toDouble()))
            val blockUp2 = level.getBlock(Vector3(x.toDouble(), (y + 2).toDouble(), z.toDouble()))

            if (blockUp.isSolid || blockUp is BlockLiquid ||
                blockUp2.isSolid || blockUp2 is BlockLiquid
            ) {
                continue
            }

            // Sounds are broadcast at both source and destination
            level.addLevelSoundEvent(player.position, LevelSoundEventPacket.SOUND_TELEPORT)
            player.teleport(
                Vector3(x + 0.5, (y + 1).toDouble(), z + 0.5),
                PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT
            )
            level.addLevelSoundEvent(player.position, LevelSoundEventPacket.SOUND_TELEPORT)

            break
        }

        return true
    }
}
