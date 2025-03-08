package cn.nukkit.event.entity

import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityInventoryChangeEvent(entity: Entity?, oldItem: Item, newItem: Item, slot: Int) :
    EntityEvent(), Cancellable {
    val oldItem: Item
    @JvmField
    var newItem: Item
    val slot: Int

    init {
        this.entity = entity
        this.oldItem = oldItem
        this.newItem = newItem
        this.slot = slot
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
