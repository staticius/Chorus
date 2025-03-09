package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

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
