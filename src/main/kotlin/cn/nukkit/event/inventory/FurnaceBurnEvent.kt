package cn.nukkit.event.inventory

import cn.nukkit.blockentity.BlockEntityFurnace
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.event.block.BlockEvent
import cn.nukkit.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class FurnaceBurnEvent(val furnace: BlockEntityFurnace, val fuel: Item, var burnTime: Int) : BlockEvent(
    furnace.block
),
    Cancellable {
    var isBurning: Boolean = true

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
