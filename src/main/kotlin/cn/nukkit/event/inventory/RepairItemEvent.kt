package cn.nukkit.event.inventory

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.AnvilInventory
import cn.nukkit.item.Item

class RepairItemEvent(
    inventory: AnvilInventory,
    val oldItem: Item,
    val newItem: Item,
    val materialItem: Item,
    var cost: Int,
    val player: Player
) :
    InventoryEvent(inventory), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
