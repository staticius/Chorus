package org.chorus.event.inventory

import org.chorus.blockentity.BlockEntityFurnace
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.block.BlockEvent
import org.chorus.item.Item


class FurnaceSmeltEvent(furnace: BlockEntityFurnace, source: Item, result: Item, xp: Float) :
    BlockEvent(furnace.block), Cancellable {
    val furnace: BlockEntityFurnace
    val source: Item = source.clone()
    var result: Item
    var xp: Float

    init {
        this.source.setCount(1)
        this.result = result
        this.furnace = furnace
        this.xp = xp
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
