package org.chorus.event.entity

import org.chorus.block.Block
import org.chorus.entity.Entity

/**
 * @author Box (Nukkit Project)
 */
class EntityCombustByBlockEvent(val combuster: Block, combustee: Entity?, duration: Int) :
    EntityCombustEvent(combustee, duration)
