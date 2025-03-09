package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.block.BlockLiquid
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class LiquidFlowEvent(val to: Block, val source: BlockLiquid, val newFlowDecay: Int) : BlockEvent(
    to
), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
