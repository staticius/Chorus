package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus_oss.chorus.network.protocol.types.itemstack.ContainerSlotType


class InventorySlotPacket : DataPacket(), PacketEncoder {
    var inventoryId: Int = 0
    var slot: Int = 0
    var fullContainerName: FullContainerName = FullContainerName(ContainerSlotType.ANVIL_INPUT, null)
    var storageItem: Item = Item.AIR // is air if the item is not a bundle
    var item: Item? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.inventoryId)
        byteBuf.writeUnsignedVarInt(this.slot)
        byteBuf.writeFullContainerName(this.fullContainerName)
        byteBuf.writeSlot(this.storageItem)
        byteBuf.writeSlot(this.item)
    }

    override fun pid(): Int {
        return ProtocolInfo.INVENTORY_SLOT_PACKET
    }

    override fun toString(): String {
        return "InventorySlotPacket(inventoryId=$inventoryId, slot=$slot, fullContainerName=$fullContainerName, storageItem=$storageItem, item=$item)"
    }
}
