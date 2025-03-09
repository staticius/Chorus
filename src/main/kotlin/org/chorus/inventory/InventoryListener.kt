package org.chorus.inventory

import cn.nukkit.item.Item


interface InventoryListener {
    fun onInventoryChanged(inventory: Inventory?, oldItem: Item?, slot: Int)
}
