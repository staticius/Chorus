package org.chorus.inventory.request

import com.google.common.collect.Lists
import org.chorus.Player
import org.chorus.entity.EntityHumanType.getInventory
import org.chorus.inventory.*
import org.chorus.network.protocol.types.itemstack.request.action.DestroyAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import java.util.List

/**
 * Allay Project 2023/7/28
 *
 * @author daoge_cmd
 */

class DestroyActionProcessor : ItemStackRequestActionProcessor<DestroyAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.DESTROY

    override fun handle(action: DestroyAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val noResponseForDestroyAction =
            context.get<Boolean>(CraftResultDeprecatedActionProcessor.Companion.NO_RESPONSE_DESTROY_KEY)
        if (noResponseForDestroyAction != null && noResponseForDestroyAction) {
            return null
        }
        if (player.gamemode != 1) {
            DestroyActionProcessor.log.warn("only creative mode can destroy item")
            return context.error()
        }
        val container = action.source.container
        val sourceInventory: Inventory = getInventory(player, container)
        val count = action.count
        val slot = sourceInventory.fromNetworkSlot(action.source.slot)
        var item = sourceInventory.getItem(slot)
        if (validateStackNetworkId(item.netId, action.source.stackNetworkId)) {
            DestroyActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        if (item.isNull) {
            DestroyActionProcessor.log.warn("cannot destroy an air!")
            return context.error()
        }
        if (item.getCount() < count) {
            DestroyActionProcessor.log.warn("cannot destroy more items than the current amount!")
            return context.error()
        }
        if (item.getCount() > count) {
            item.setCount(item.getCount() - count)
            sourceInventory.setItem(slot, item, false)
        } else {
            sourceInventory.clear(slot, false)
            item = sourceInventory.getItem(slot)
        }
        return context.success(
            List.of(
                ItemStackResponseContainer(
                    sourceInventory.getSlotType(slot),
                    Lists.newArrayList(
                        ItemStackResponseSlot(
                            sourceInventory.toNetworkSlot(slot),
                            sourceInventory.toNetworkSlot(slot),
                            item.getCount(),
                            item.netId,
                            item.customName,
                            item.damage
                        )
                    ),
                    action.source.containerName
                )
            )
        )
    }
}
