package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.math.Vector3

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class EntityMotionEvent(entity: Entity?, motion: Vector3) : EntityEvent(), Cancellable {
    val motion: Vector3

    init {
        this.entity = entity
        this.motion = motion
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
