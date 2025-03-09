package org.chorus.event.entity

import org.chorus.entity.Entity

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityDamageByChildEntityEvent(
    damager: Entity,
    val child: Entity,
    entity: Entity,
    cause: DamageCause,
    damage: Float
) :
    EntityDamageByEntityEvent(damager, entity, cause, damage)
