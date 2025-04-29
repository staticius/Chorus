package org.chorus_oss.chorus.event.entity

import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.entity.Entity

class EntityCombustByBlockEvent(val block: Block, entity: Entity, duration: Int) :
    EntityCombustEvent(entity, duration)
