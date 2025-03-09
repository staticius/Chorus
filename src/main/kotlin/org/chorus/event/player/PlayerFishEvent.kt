package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.entity.item.EntityFishingHook
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item
import cn.nukkit.math.Vector3

/**
 * An event that is called when player catches a fish
 *
 * @author PetteriM1
 */
class PlayerFishEvent(player: Player?, hook: EntityFishingHook, loot: Item, experience: Int, motion: Vector3) :
    PlayerEvent(), Cancellable {
    val hook: EntityFishingHook
    var loot: Item
    var experience: Int
    var motion: Vector3

    init {
        this.player = player
        this.hook = hook
        this.loot = loot
        this.experience = experience
        this.motion = motion
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
