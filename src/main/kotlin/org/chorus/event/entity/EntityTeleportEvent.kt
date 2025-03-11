package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.player.PlayerTeleportEvent.TeleportCause
import org.chorus.level.Transform


class EntityTeleportEvent @JvmOverloads constructor(
    entity: Entity?,
    from: Transform,
    to: Transform,
    cause: TeleportCause = TeleportCause.UNKNOWN
) :
    EntityEvent(), Cancellable {
    var from: Transform
    var to: Transform
    val cause: TeleportCause

    init {
        this.entity = entity
        this.from = from
        this.to = to
        this.cause = cause
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
