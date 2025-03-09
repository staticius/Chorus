package org.chorus.event.block

import org.chorus.block.BlockHopper.IHopper
import org.chorus.event.Cancellable
import org.chorus.event.Event
import org.chorus.event.HandlerList

class HopperSearchItemEvent(val hopper: IHopper, val isMinecart: Boolean) : Event(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
