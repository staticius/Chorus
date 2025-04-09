package org.chorus.event.player

import org.chorus.Player
import org.chorus.entity.item.EntityFishingHook
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item
import org.chorus.math.Vector3

class PlayerFishEvent(player: Player, hook: EntityFishingHook, loot: Item, experience: Int, motion: Vector3) :
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
