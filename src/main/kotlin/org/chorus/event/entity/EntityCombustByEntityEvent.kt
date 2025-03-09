package org.chorus.event.entity

import org.chorus.entity.Entity

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityCombustByEntityEvent(val combuster: Entity, combustee: Entity?, duration: Int) :
    EntityCombustEvent(combustee, duration)
