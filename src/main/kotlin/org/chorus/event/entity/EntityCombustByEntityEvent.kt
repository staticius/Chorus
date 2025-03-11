package org.chorus.event.entity

import org.chorus.entity.Entity


class EntityCombustByEntityEvent(val combuster: Entity, combustee: Entity?, duration: Int) :
    EntityCombustEvent(combustee, duration)
