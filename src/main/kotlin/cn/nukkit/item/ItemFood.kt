package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.player.PlayerItemConsumeEvent
import cn.nukkit.level.Sound
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.CompletedUsingItemPacket

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class ItemFood : Item {
    constructor(id: String) : super(id)

    constructor(id: String, meta: Int) : super(id, meta)

    constructor(id: String, meta: Int, count: Int) : super(id, meta, count)

    constructor(id: String, meta: Int, count: Int, name: String?) : super(id, meta, count, name)

    open val foodRestore: Int
        get() = 0

    open val saturationRestore: Float
        get() = 0f

    open val isRequiresHunger: Boolean
        get() = true

    open val eatingTicks: Int
        get() = 31

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        if (player.foodData.isHungry || !this.isRequiresHunger || player.isCreative) {
            return true
        }
        player.foodData.sendFood()
        return false
    }

    override fun onUse(player: Player, ticksUsed: Int): Boolean {
        if (ticksUsed < eatingTicks) {
            return false
        }

        val event = PlayerItemConsumeEvent(player, this)
        Server.getInstance().pluginManager.callEvent(event)

        if (event.isCancelled) {
            player.getInventory().sendContents(player)
            return false
        }

        if (this.onEaten(player)) {
            player.foodData.addFood(this)
            player.completeUsingItem(this.runtimeId, CompletedUsingItemPacket.ACTION_EAT)

            if (player.isAdventure || player.isSurvival) {
                --this.count
                player.getInventory().setItemInHand(this)

                player.level!!.addSound(player.position, Sound.RANDOM_BURP)
            }
        }

        player.level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                player,
                player.position.add(0.0, player.getEyeHeight().toDouble()),
                VibrationType.EAT
            )
        )

        return true
    }

    /*
     * Used for additional behaviour in Food like: Chorus, Suspicious Stew and etc.
     */
    open fun onEaten(player: Player): Boolean {
        return true
    }
}
