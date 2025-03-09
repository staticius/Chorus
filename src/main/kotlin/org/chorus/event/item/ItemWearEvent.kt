package org.chorus.event.item

import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

class ItemWearEvent(item: Item, @JvmField var newDurability: Int) : ItemEvent(item), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
