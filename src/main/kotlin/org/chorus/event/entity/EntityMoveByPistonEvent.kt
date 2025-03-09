package org.chorus.event.entity

import org.chorus.entity.Entity
import org.chorus.math.Vector3

class EntityMoveByPistonEvent(entity: Entity?, pos: Vector3) :
    EntityMotionEvent(entity, pos)
