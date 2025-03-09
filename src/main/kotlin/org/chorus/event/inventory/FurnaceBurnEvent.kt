package org.chorus.event.inventory

import org.chorus.blockentity.BlockEntityFurnace
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.block.BlockEvent
import org.chorus.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class FurnaceBurnEvent(val furnace: BlockEntityFurnace, val fuel: Item, var burnTime: Int) : BlockEvent(
    furnace.block
),
    Cancellable {
    var isBurning: Boolean = true

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
