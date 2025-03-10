package org.chorus.event.inventory

import org.chorus.blockentity.BlockEntityCampfire
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.event.block.BlockEvent
import org.chorus.item.Item


class CampfireSmeltEvent(campfire: BlockEntityCampfire, source: Item, result: Item) :
    BlockEvent(campfire.block), Cancellable {
    val campfire: BlockEntityCampfire
    val source: Item = source.clone()
    var result: Item
    var keepItem: Boolean = false

    init {
        this.source.setCount(1)
        this.result = result
        this.campfire = campfire
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
