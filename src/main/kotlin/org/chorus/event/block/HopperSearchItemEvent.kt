package org.chorus.event.block

import cn.nukkit.block.BlockHopper.IHopper
import cn.nukkit.event.Cancellable
import cn.nukkit.event.Event
import cn.nukkit.event.HandlerList

class HopperSearchItemEvent(val hopper: IHopper, val isMinecart: Boolean) : Event(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
