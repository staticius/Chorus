package org.chorus.event.player

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.entity.EntityEvent

class EntityFreezeEvent(human: Entity?) : EntityEvent(), Cancellable {
    init {
        this.entity = human
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
