package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Event

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class EntityEvent : Event() {
    open var entity: Entity? = null
        protected set
}
