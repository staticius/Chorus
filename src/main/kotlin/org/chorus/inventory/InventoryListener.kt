package org.chorus.inventory

import org.chorus.item.Item


interface InventoryListener {
    fun onInventoryChanged(inventory: Inventory?, oldItem: Item?, slot: Int)
}
