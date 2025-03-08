package cn.nukkit.inventory.request

import cn.nukkit.Player
import cn.nukkit.entity.EntityHumanType.getInventory
import cn.nukkit.inventory.*
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import cn.nukkit.network.protocol.types.itemstack.request.action.TakeAction
import lombok.extern.slf4j.Slf4j


/**
 * Allay Project 2023/7/28
 *
 * @author daoge_cmd
 */
@Slf4j
class TakeActionProcessor : TransferItemActionProcessor<TakeAction>() {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.TAKE

    override fun handle(action: TakeAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val sourceSlotType = action.source.container
        if (sourceSlotType == ContainerSlotType.CREATED_OUTPUT) {
            val source: Inventory = getInventory(player, sourceSlotType)
            val sourItem = source.getUnclonedItem(0)
            val count = action.count
            if (sourItem!!.getCount() > count) {
                sourItem.setCount(count)
            }
        }
        return super.handle(action, player, context)
    }
}
