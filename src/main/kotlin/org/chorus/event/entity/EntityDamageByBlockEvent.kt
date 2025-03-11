package org.chorus.event.entity

import org.chorus.block.Block
import org.chorus.entity.Entity


class EntityDamageByBlockEvent(@JvmField val damager: Block, entity: Entity, cause: DamageCause?, damage: Float) :
    EntityDamageEvent(entity, cause, damage)
