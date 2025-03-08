package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class EntityCombustEvent(combustee: Entity?, duration: Int) : EntityEvent(), Cancellable {
    @JvmField
    var duration: Int

    init {
        this.entity = combustee
        this.duration = duration
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
