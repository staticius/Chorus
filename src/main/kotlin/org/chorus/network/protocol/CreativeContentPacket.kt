package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.inventory.creative.CreativeItemData
import org.chorus.network.protocol.types.inventory.creative.CreativeItemGroup
import org.chorus.registry.CreativeItemRegistry
import org.chorus.registry.Registries
import java.util.function.BiConsumer


class CreativeContentPacket : DataPacket() {
    private val groups: List<CreativeItemGroup> = ObjectArrayList()
    private val contents: List<CreativeItemData> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(CreativeItemRegistry.creativeGroups) { buf, group ->
            this.writeGroup(buf, group)
        }
        byteBuf.writeArray(CreativeItemRegistry.creativeItemData) { buf, data ->
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
