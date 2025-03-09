package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.block.property.enums.CauldronLiquid
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class CauldronFilledByDrippingLiquidEvent(cauldron: Block, @JvmField var liquid: CauldronLiquid, @JvmField var liquidLevelIncrement: Int) :
    BlockEvent(cauldron), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
