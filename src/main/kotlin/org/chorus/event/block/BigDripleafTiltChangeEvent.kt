package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.block.property.enums.BigDripleafTilt
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class BigDripleafTiltChangeEvent(block: Block, val oldTilt: BigDripleafTilt, var newTilt: BigDripleafTilt) :
    BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
