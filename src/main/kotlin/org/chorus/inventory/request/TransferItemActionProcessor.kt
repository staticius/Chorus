package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.entity.EntityHumanType.getInventory
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.network.protocol.types.itemstack.request.action.TransferItemStackRequestAction
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import java.util.List


abstract class TransferItemActionProcessor<T : TransferItemStackRequestAction?> : ItemStackRequestActionProcessor<T> {
    override fun handle(action: T, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val sourceSlotType = action!!.source.container
        val destinationSlotType = action.destination.container
        val source: Inventory = getInventory(player, sourceSlotType)
        val destination: Inventory = getInventory(player, destinationSlotType)
        val sourceSlot = source.fromNetworkSlot(action.source.slot)
        val sourceStackNetworkId = action.source.stackNetworkId
        val destinationSlot = destination.fromNetworkSlot(action.destination.slot)
        val destinationStackNetworkId = action.destination.stackNetworkId
        val count = action.count

        var sourItem = source.getItem(sourceSlot)
        if (sourItem.isNothing) {
            TransferItemActionProcessor.log.warn("transfer an air item is not allowed")
            return context.error()
        }
        if (validateStackNetworkId(sourItem.netId, sourceStackNetworkId)) {
            TransferItemActionProcessor.log.warn("mismatch source stack network id!")
            return context.error()
        }
        if (sourItem.getCount() < count) {
            TransferItemActionProcessor.log.warn(
                "transfer an item that has not enough count is not allowed. Expected: {}, Actual: {}",
                sourItem.getCount(),
                count
            )
            return context.error()
        }

        if (context.has(CraftCreativeActionProcessor.Companion.CRAFT_CREATIVE_KEY) && (context.get<Any>(
                CraftCreativeActionProcessor.Companion.CRAFT_CREATIVE_KEY
            ) as Boolean)
        ) { //If the player takes an item from creative mode, the destination is overridden directly
            if (source is CreativeOutputInventory) {
                sourItem = sourItem.clone().autoAssignStackNetworkId()
                destination.setItem(destinationSlot, sourItem, false)
                return context.success(
                    List.of(
                        ItemStackResponseContainer(
                            destination.getSlotType(destinationSlot),
                            listOf(
                                ItemStackResponseSlot(
                                    destination.toNetworkSlot(destinationSlot),
                                    destination.toNetworkSlot(destinationSlot),
                                    sourItem.getCount(),
                                    sourItem.netId,
                                    sourItem.customName,
                                    sourItem.damage
                                )
                            ),
                            action.destination.containerName
                        )
                    )
                )
            }
        }

        val destItem = destination.getItem(destinationSlot)
        if (!destItem.isNothing && !destItem.equals(sourItem, true, true)) {
            TransferItemActionProcessor.log.warn("transfer an item to a slot that has a different item is not allowed")
            return context.error()
        }
        if (validateStackNetworkId(destItem.netId, destinationStackNetworkId)) {
            TransferItemActionProcessor.log.warn("mismatch destination stack network id!")
            return context.error()
        }
        if (destItem.getCount() + count > destItem.maxStackSize) {
            TransferItemActionProcessor.log.warn("destination stack size bigger than the max stack size!")
            return context.error()
        }

        val resultSourItem: Item
        val resultDestItem: Item
        val sendSource =
            false //Previous "!(source instanceof SoleInventory);", Not sending the source fixes the drag item distribution. Shouldn't cause any problems because inventory management is serversided.
        val sendDest = destination !is SoleInventory
        //first case：transfer all item
        if (sourItem.getCount() == count) {
            source.clear(sourceSlot, sendSource)
            resultSourItem = source.getItem(sourceSlot)
            if (!destItem.isNothing) {
                //目标物品不为空，直接添加数量，目标物品网络堆栈id不变
                resultDestItem = destItem
                resultDestItem.setCount(destItem.getCount() + count)
                destination.setItem(destinationSlot, resultDestItem, sendDest)
            } else {
                //目标物品为空，直接移动原有堆栈到新位置，网络堆栈id使用源物品的网络堆栈id（相当于换个位置）
                if (source is CreativeOutputInventory) {
                    //HACK: 若是从CREATED_OUTPUT拿出的，需要服务端自行新建个网络堆栈id
                    sourItem = sourItem.clone().autoAssignStackNetworkId()
                }
                resultDestItem = sourItem
                destination.setItem(destinationSlot, resultDestItem, sendDest)
            }
        } else { //second case：transfer a part of item
            resultSourItem = sourItem
            resultSourItem.setCount(resultSourItem.getCount() - count)
            source.setItem(sourceSlot, resultSourItem, sendSource) //减少源库存数量
            if (!destItem.isNothing) { //目标物品不为空
                resultDestItem = destItem
                resultDestItem.setCount(destItem.getCount() + count) //增加目的库存数量
                destination.setItem(destinationSlot, resultDestItem, sendDest)
            } else { //目标物品为空，为分出来的子物品堆栈新建网络堆栈id
                resultDestItem = sourItem.clone().autoAssignStackNetworkId()
                resultDestItem.setCount(count)
                destination.setItem(destinationSlot, resultDestItem, sendDest)
            }
        }
        val destItemStackResponseSlot =
            ItemStackResponseContainer(
                destination.getSlotType(destinationSlot),
                listOf(
                    ItemStackResponseSlot(
                        destination.toNetworkSlot(destinationSlot),
                        destination.toNetworkSlot(destinationSlot),
                        resultDestItem.getCount(),
                        resultDestItem.netId,
                        resultDestItem.customName,
                        resultDestItem.damage
                    )
                ),
                action.destination.containerName
            )
        //CREATED_OUTPUT不需要发source响应
        return if (source is CreativeOutputInventory) {
            context.success(List.of(destItemStackResponseSlot))
        } else {
            context.success(
                List.of(
                    ItemStackResponseContainer(
                        source.getSlotType(sourceSlot),
                        listOf(
                            ItemStackResponseSlot(
                                source.toNetworkSlot(sourceSlot),
                                source.toNetworkSlot(sourceSlot),
                                resultSourItem.getCount(),
                                resultSourItem.netId,
                                resultSourItem.customName,
                                resultSourItem.damage
                            )
                        ),
                        action.source.containerName
                    ), destItemStackResponseSlot
                )
            )
        }
    }
}
