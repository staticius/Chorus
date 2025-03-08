package cn.nukkit.event.inventory

import cn.nukkit.blockentity.BlockEntityCampfire
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.block.BlockEvent
import cn.nukkit.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class CampfireSmeltEvent(campfire: BlockEntityCampfire, source: Item, result: Item) :
    BlockEvent(campfire.block), Cancellable {
    val campfire: BlockEntityCampfire
    val source: Item = source.clone()
    var result: Item
    var keepItem: Boolean = false

    init {
        this.source.setCount(1)
        this.result = result
        this.campfire = campfire
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
