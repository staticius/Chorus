package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus_oss.chorus.network.protocol.types.itemstack.ContainerSlotType

class InventoryContentPacket : DataPacket() {
    var inventoryId: Int = 0
    var slots: List<Item> = emptyList()
    var fullContainerName: FullContainerName = FullContainerName(ContainerSlotType.ANVIL_INPUT, null)
    var storageItem: Item = Item.AIR // is air if the item is not a bundle

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.inventoryId)
        byteBuf.writeUnsignedVarInt(slots.size)
        for (slot in this.slots) {
            byteBuf.writeSlot(slot)
        }
        byteBuf.writeFullContainerName(this.fullContainerName)
        byteBuf.writeSlot(this.storageItem)
    }

    override fun pid(): Int {
        return ProtocolInfo.INVENTORY_CONTENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    override fun toString(): String {
        return "InventoryContentPacket(inventoryId=$inventoryId, slots=$slots, fullContainerName=$fullContainerName, storageItem=$storageItem)"
    }
}
