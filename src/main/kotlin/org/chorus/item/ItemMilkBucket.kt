package org.chorus.item

import org.chorus.Player
import org.chorus.Server
import org.chorus.event.player.PlayerItemConsumeEvent
import org.chorus.level.Sound
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.Vector3
import org.chorus.network.protocol.CompletedUsingItemPacket

class ItemMilkBucket : ItemBucket(ItemID.Companion.MILK_BUCKET) {
    override var damage: Int
        get() = super.damage
        set(meta) {
        }

    override fun onUse(player: Player, ticksUsed: Int): Boolean {
        if (ticksUsed < 31) {
            return false
        }

        val event = PlayerItemConsumeEvent(player, this)
        Server.instance.pluginManager.callEvent(event)

        if (event.isCancelled) {
            player.getInventory().sendContents(player)
            return false
        }

        player.removeAllEffects()

        player.completeUsingItem(this.runtimeId, CompletedUsingItemPacket.ACTION_EAT)

        if (player.isAdventure || player.isSurvival) {
            --this.count
            player.getInventory().addItem(get(ItemID.Companion.BUCKET, 0, 1))
            player.getInventory().setItemInHand(this)
            player.level!!.addSound(player.position, Sound.RANDOM_BURP)
        }

        player.level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                player,
                player.position.add(0.0, player.getEyeHeight().toDouble()),
                VibrationType.DRINKING
            )
        )

        return true
    }

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return true
    }
}