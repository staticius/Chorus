package org.chorus.inventory.fake

import org.chorus.event.inventory.ItemStackRequestActionEvent
import org.chorus.item.Item

fun interface ItemHandler {
    fun handle(
        fakeInventory: FakeInventory?,
        slot: Int,
        oldItem: Item?,
        newItem: Item?,
        event: ItemStackRequestActionEvent?
    )
}
