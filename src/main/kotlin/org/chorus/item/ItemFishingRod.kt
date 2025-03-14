package org.chorus.item

import org.chorus.Player
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.Vector3

/**
 * @author Snake1999
 * @since 2016/1/14
 */
class ItemFishingRod @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.FISHING_ROD, meta, count, "Fishing Rod") {
    override val enchantAbility: Int
        get() = 1

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        if (player.fishing != null) {
            player.stopFishing(true)
            player.level!!.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    player,
                    player.position.clone(),
                    VibrationType.ITEM_INTERACT_FINISH
                )
            )
        } else {
            player.startFishing(this)
            meta++
        }
        return true
    }

    override val maxDurability: Int
        get() = DURABILITY_FISHING_ROD

    override fun noDamageOnAttack(): Boolean {
        return true
    }

    override fun noDamageOnBreak(): Boolean {
        return true
    }
}
