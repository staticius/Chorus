package org.chorus.inventory

import org.chorus.item.Item


fun interface InventoryListener {
    fun onInventoryChanged(inventory: Inventory?, oldItem: Item?, slot: Int)
}
