package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.inventory.creative.CreativeItemData
import cn.nukkit.network.protocol.types.inventory.creative.CreativeItemGroup
import cn.nukkit.registry.Registries
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.util.function.BiConsumer

@Getter
@Setter
@ToString
@NoArgsConstructor
class CreativeContentPacket : DataPacket() {
    private val groups: List<CreativeItemGroup> = ObjectArrayList()
    private val contents: List<CreativeItemData> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(
            Registries.CREATIVE.creativeGroups,
            BiConsumer { byteBuf: HandleByteBuf, group: CreativeItemGroup -> this.writeGroup(byteBuf, group) })
        byteBuf.writeArray(
            Registries.CREATIVE.creativeItemData,
            BiConsumer { byteBuf: HandleByteBuf, data: CreativeItemData -> this.writeItem(byteBuf, data) })
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
        return ProtocolInfo.Companion.CREATIVE_CONTENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
