package org.chorus.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.math.Vector3

class EntityMoveByPistonEvent(entity: Entity?, pos: Vector3) :
    EntityMotionEvent(entity, pos)
