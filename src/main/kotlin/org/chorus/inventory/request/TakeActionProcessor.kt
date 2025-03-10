package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.entity.EntityHumanType.getInventory
import org.chorus.inventory.*
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.request.action.TakeAction



/**
 * Allay Project 2023/7/28
 *
 * @author daoge_cmd
 */

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
