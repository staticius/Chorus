package org.chorus.event.inventory

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.EnchantInventory
import cn.nukkit.item.Item

class EnchantItemEvent(
    inventory: EnchantInventory,
    var oldItem: Item,
    var newItem: Item,
    var xpCost: Int,
    var enchanter: Player
) :
    InventoryEvent(inventory), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
