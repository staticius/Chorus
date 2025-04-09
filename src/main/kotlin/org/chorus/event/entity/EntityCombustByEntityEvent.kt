package org.chorus.event.entity

import org.chorus.entity.Entity


class EntityCombustByEntityEvent(val combusterEntity: Entity, combustingEntity: Entity, duration: Int) :
    EntityCombustEvent(combustingEntity, duration)
