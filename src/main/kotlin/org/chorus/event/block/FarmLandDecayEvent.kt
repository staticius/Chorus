package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.entity.Entity
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class FarmLandDecayEvent(val entity: Entity?, farm: Block) : BlockEvent(farm), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
