package org.chorus.event.player

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.entity.EntityEvent

class EntityFreezeEvent(human: Entity?) : EntityEvent(), Cancellable {
    init {
        this.entity = human
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
