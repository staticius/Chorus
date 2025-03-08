package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.player.PlayerTeleportEvent.TeleportCause
import cn.nukkit.level.Transform

/**
 * @author MagicDroidX (Nukkit Project)
 */
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
