package org.chorus.event.player

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class PlayerChangeArmorStandEvent(player: Player?, armorStand: Entity, item: Item, slot: Int) :
    PlayerEvent(), Cancellable {
    val armorStand: Entity
    val slot: Int
    var item: Item

    init {
        this.player = player
        this.armorStand = armorStand
        this.item = item
        this.slot = slot
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
