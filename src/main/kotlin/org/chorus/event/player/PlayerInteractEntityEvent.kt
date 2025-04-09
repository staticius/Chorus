package org.chorus.event.player

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item
import org.chorus.math.Vector3

class PlayerInteractEntityEvent(player: Player, entity: Entity, item: Item, clickedPos: Vector3) :
    PlayerEvent(), Cancellable {
    val entity: Entity
    val item: Item
    val clickedPos: Vector3

    init {
        this.player = player
        this.entity = entity
        this.item = item
        this.clickedPos = clickedPos
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
