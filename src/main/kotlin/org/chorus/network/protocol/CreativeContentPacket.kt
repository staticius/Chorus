package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.inventory.creative.CreativeItemData
import org.chorus.network.protocol.types.inventory.creative.CreativeItemGroup
import org.chorus.registry.CreativeItemRegistry
import org.chorus.registry.Registries

data class CreativeContentPacket(
    val groups: List<CreativeItemGroup> = CreativeItemRegistry.creativeGroups.toList(),
    val writeEntries: List<CreativeItemData> = CreativeItemRegistry.creativeItemData.toList()
) : DataPacket() {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(this.groups) { buf, group ->
            this.writeGroup(buf, group)
        }
        byteBuf.writeArray(this.writeEntries) { buf, data ->
            this.writeItem(buf, data)
        }
    }

    private fun writeGroup(byteBuf: HandleByteBuf, group: CreativeItemGroup) {
        byteBuf.writeIntLE(group.category.ordinal)
        byteBuf.writeString(group.name)
        byteBuf.writeSlot(group.icon, true)
    }

    private fun writeItem(byteBuf: HandleByteBuf, data: CreativeItemData) {
        byteBuf.writeUnsignedVarInt(Registries.CREATIVE.getCreativeItemIndex(data.item))
        byteBuf.writeSlot(data.item, true)
        byteBuf.writeUnsignedVarInt(data.groupId)
    }

    override fun pid(): Int {
        return ProtocolInfo.CREATIVE_CONTENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
