package org.chorus.inventory.fake

import cn.nukkit.event.inventory.ItemStackRequestActionEvent
import cn.nukkit.item.Item

fun interface ItemHandler {
    fun handle(
        fakeInventory: FakeInventory?,
        slot: Int,
        oldItem: Item?,
        newItem: Item?,
        event: ItemStackRequestActionEvent?
    )
}
