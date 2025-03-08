package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*

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
        get() = ItemTool.Companion.DURABILITY_FISHING_ROD

    override fun noDamageOnAttack(): Boolean {
        return true
    }

    override fun noDamageOnBreak(): Boolean {
        return true
    }
}
