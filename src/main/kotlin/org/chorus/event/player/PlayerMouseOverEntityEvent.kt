package org.chorus.event.player

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.event.HandlerList

class PlayerMouseOverEntityEvent(player: Player?, entity: Entity) : PlayerEvent() {
    val entity: Entity

    init {
        this.player = player
        this.entity = entity
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
