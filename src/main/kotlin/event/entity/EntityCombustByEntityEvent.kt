package org.chorus_oss.chorus.event.entity

import org.chorus_oss.chorus.entity.Entity


class EntityCombustByEntityEvent(val combusterEntity: Entity, combustingEntity: Entity, duration: Int) :
    EntityCombustEvent(combustingEntity, duration)
