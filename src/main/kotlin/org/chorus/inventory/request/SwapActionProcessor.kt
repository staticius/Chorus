package org.chorus.inventory.request

import cn.nukkit.Player
import cn.nukkit.entity.EntityHumanType.getInventory
import cn.nukkit.inventory.*
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import cn.nukkit.network.protocol.types.itemstack.request.action.SwapAction
import cn.nukkit.network.protocol.types.itemstack.response.ItemStackResponseContainer
import cn.nukkit.network.protocol.types.itemstack.response.ItemStackResponseSlot
import com.google.common.collect.Lists
import lombok.extern.slf4j.Slf4j
import java.util.List

/**
 * Allay Project 2023/10/7
 *
 * @author daoge_cmd
 */
@Slf4j
class SwapActionProcessor : ItemStackRequestActionProcessor<SwapAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.SWAP

    override fun handle(action: SwapAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val sourceSlotType = action.source.container
        val destinationSlotType = action.destination.container
        val source: Inventory = getInventory(player, sourceSlotType)
        val destination: Inventory = getInventory(player, destinationSlotType)

        val sourceSlot = source.fromNetworkSlot(action.source.slot)
        val destinationSlot = destination.fromNetworkSlot(action.destination.slot)
        val sourceItem = source.getItem(sourceSlot)
        val destinationItem = destination.getItem(destinationSlot)
        if (validateStackNetworkId(sourceItem.netId, action.source.stackNetworkId)) {
            SwapActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        if (validateStackNetworkId(destinationItem.netId, action.destination.stackNetworkId)) {
            SwapActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        source.setItem(sourceSlot, destinationItem, false)
        destination.setItem(destinationSlot, sourceItem, false)
        return context.success(
            List.of(
                ItemStackResponseContainer(
                    source.getSlotType(sourceSlot),
                    Lists.newArrayList(
                        ItemStackResponseSlot(
                            source.toNetworkSlot(sourceSlot),
                            source.toNetworkSlot(sourceSlot),
                            destinationItem.getCount(),
                            destinationItem.netId,
                            destinationItem.customName,
                            destinationItem.damage
                        )
                    ),
                    action.source.containerName
                ),
                ItemStackResponseContainer(
                    destination.getSlotType(destinationSlot),
                    Lists.newArrayList(
                        ItemStackResponseSlot(
                            destination.toNetworkSlot(destinationSlot),
                            destination.toNetworkSlot(destinationSlot),
                            sourceItem.getCount(),
                            sourceItem.netId,
                            sourceItem.customName,
                            sourceItem.damage
                        )
                    ),
                    action.destination.containerName
                )
            )
        )
    }
}
