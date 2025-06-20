package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.inventory.creative.CreativeItemData
import org.chorus_oss.chorus.network.protocol.types.inventory.creative.CreativeItemGroup
import org.chorus_oss.chorus.registry.CreativeItemRegistry
import org.chorus_oss.chorus.utils.Loggable

data class CreativeContentPacket(
    val groups: List<CreativeItemGroup> = CreativeItemRegistry.creativeGroups,
    val writeEntries: List<CreativeItemData> = CreativeItemRegistry.creativeItemData
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
        byteBuf.writeUnsignedVarInt(data.netID)
        byteBuf.writeSlot(data.item, true)
        byteBuf.writeUnsignedVarInt(data.groupId)
    }

    override fun pid(): Int {
        return ProtocolInfo.CREATIVE_CONTENT_PACKET
    }

    companion object : Loggable
}
