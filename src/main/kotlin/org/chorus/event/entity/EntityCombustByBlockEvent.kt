package org.chorus.event.entity

import org.chorus.block.Block
import org.chorus.entity.Entity

class EntityCombustByBlockEvent(val block: Block, entity: Entity, duration: Int) :
    EntityCombustEvent(entity, duration)
