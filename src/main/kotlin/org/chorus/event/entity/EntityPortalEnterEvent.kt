package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class EntityPortalEnterEvent(entity: Entity?, type: PortalType) : EntityEvent(), Cancellable {
    val portalType: PortalType

    init {
        this.entity = entity
        this.portalType = type
    }

    enum class PortalType {
        NETHER,
        END
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
