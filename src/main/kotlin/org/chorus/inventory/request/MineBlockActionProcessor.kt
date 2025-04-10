package org.chorus.inventory.request

import com.google.common.collect.Lists
import org.chorus.Player
import org.chorus.inventory.SpecialWindowId
import org.chorus.network.protocol.InventorySlotPacket
import org.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.request.action.MineBlockAction
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import java.util.List


class MineBlockActionProcessor : ItemStackRequestActionProcessor<MineBlockAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.MINE_BLOCK

    override fun handle(action: MineBlockAction, player: Player, context: ItemStackRequestContext): ActionResponse {
        val inventory = player.inventory
        val heldItemIndex = inventory.heldItemIndex
        if (heldItemIndex != action.hotbarSlot) {
            MineBlockActionProcessor.log.warn("The held Item Index on the server side does not match the client side!")
            return context.error()
        }

        val itemInHand = inventory.itemInHand
        if (validateStackNetworkId(itemInHand.netId, action.stackNetworkId)) {
            MineBlockActionProcessor.log.warn("mismatch source stack network id!")
            return context.error()
        }

        if (itemInHand.damage != action.predictedDurability) {
            val inventorySlotPacket = InventorySlotPacket()
            val id = SpecialWindowId.PLAYER.id
            inventorySlotPacket.inventoryId = id
            inventorySlotPacket.item = itemInHand
            inventorySlotPacket.slot = action.hotbarSlot
            inventorySlotPacket.fullContainerName = FullContainerName(
                ContainerSlotType.HOTBAR,
                id
            )
            player.dataPacket(inventorySlotPacket)
        }
        val itemStackResponseSlot =
            ItemStackResponseContainer(
                inventory.getSlotType(heldItemIndex),
                listOf(
                    ItemStackResponseSlot(
                        inventory.toNetworkSlot(heldItemIndex),
                        inventory.toNetworkSlot(heldItemIndex),
                        itemInHand.getCount(),
                        itemInHand.netId,
                        itemInHand.customName,
                        itemInHand.damage
                    )
                ),
                FullContainerName(
                    inventory.getSlotType(heldItemIndex),
                    0 // I don't know the purpose of the dynamicId yet, this is why I leave it at 0 for the MineBlockAction
                )
            )
        return context.success(List.of(itemStackResponseSlot))
    }
}
