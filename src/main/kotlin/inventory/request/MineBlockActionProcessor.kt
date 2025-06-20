package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.experimental.network.protocol.utils.from
import org.chorus_oss.chorus.inventory.SpecialWindowId
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus_oss.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.MineBlockAction
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus_oss.chorus.utils.Loggable
import org.chorus_oss.protocol.types.item.ItemStack


class MineBlockActionProcessor : ItemStackRequestActionProcessor<MineBlockAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.MINE_BLOCK

    override fun handle(action: MineBlockAction, player: Player, context: ItemStackRequestContext): ActionResponse {
        val inventory = player.inventory
        val heldItemIndex = inventory.heldItemIndex
        if (heldItemIndex != action.hotbarSlot) {
            log.warn("The held Item Index on the server side does not match the client side!")
            return context.error()
        }

        val itemInHand = inventory.itemInHand
        if (validateStackNetworkId(itemInHand.getNetId(), action.stackNetworkId)) {
            log.warn("mismatch source stack network id!")
            return context.error()
        }

        if (itemInHand.damage != action.predictedDurability) {
            val id = SpecialWindowId.PLAYER.id
            val packet = org.chorus_oss.protocol.packets.InventorySlotPacket(
                windowID = id.toUInt(),
                slot = action.hotbarSlot.toUInt(),
                container = org.chorus_oss.protocol.types.inventory.FullContainerName(
                    org.chorus_oss.protocol.types.itemstack.ContainerSlotType.Hotbar,
                    id,
                ),
                storageItem = ItemStack.from(Item.AIR),
                newItem = ItemStack.from(itemInHand),
            )
            player.sendPacket(packet)
        }
        val itemStackResponseSlot =
            ItemStackResponseContainer(
                inventory.getSlotType(heldItemIndex),
                mutableListOf(
                    ItemStackResponseSlot(
                        inventory.toNetworkSlot(heldItemIndex),
                        inventory.toNetworkSlot(heldItemIndex),
                        itemInHand.getCount(),
                        itemInHand.getNetId(),
                        itemInHand.customName,
                        itemInHand.damage
                    )
                ),
                FullContainerName(
                    inventory.getSlotType(heldItemIndex),
                    0 // I don't know the purpose of the dynamicId yet, this is why I leave it at 0 for the MineBlockAction
                )
            )
        return context.success(listOf(itemStackResponseSlot))
    }

    companion object : Loggable
}
