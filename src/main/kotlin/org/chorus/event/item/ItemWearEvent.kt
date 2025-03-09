package org.chorus.event.item

import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class ItemWearEvent(item: Item, @JvmField var newDurability: Int) : ItemEvent(item), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
