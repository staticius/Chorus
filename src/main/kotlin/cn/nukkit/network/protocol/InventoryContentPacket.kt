package cn.nukkit.network.protocol

import cn.nukkit.item.Item
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class InventoryContentPacket : DataPacket() {
    var inventoryId: Int = 0
    var slots: Array<Item?> = Item.EMPTY_ARRAY
    var fullContainerName: FullContainerName = FullContainerName(ContainerSlotType.ANVIL_INPUT, null)
    var storageItem: Item = Item.AIR // is air if the item is not a bundle

    override fun decode(byteBuf: HandleByteBuf) {
    }

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
        return ProtocolInfo.Companion.INVENTORY_CONTENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
