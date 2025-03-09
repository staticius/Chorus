package org.chorus.network.protocol

import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class InventorySlotPacket : DataPacket() {
    var inventoryId: Int = 0
    var slot: Int = 0
    var fullContainerName: FullContainerName = FullContainerName(ContainerSlotType.ANVIL_INPUT, null)
    var storageItem: Item = Item.AIR // is air if the item is not a bundle
    var item: Item? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.inventoryId = byteBuf.readUnsignedVarInt()
        this.slot = byteBuf.readUnsignedVarInt()
        this.fullContainerName = byteBuf.readFullContainerName()
        this.storageItem = byteBuf.readSlot()
        this.item = byteBuf.readSlot()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(this.inventoryId)
        byteBuf.writeUnsignedVarInt(this.slot)
        byteBuf.writeFullContainerName(this.fullContainerName)
        byteBuf.writeSlot(this.storageItem)
        byteBuf.writeSlot(this.item)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.INVENTORY_SLOT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
