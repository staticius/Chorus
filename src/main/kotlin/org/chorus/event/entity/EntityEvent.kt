package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.event.Event

abstract class EntityEvent : Event() {
    open lateinit var entity: Entity
        protected set
}
