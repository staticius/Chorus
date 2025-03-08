package cn.nukkit.network.protocol

import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AddVolumeEntityPacket : DataPacket() {
    var id: Int = 0
    var data: CompoundTag? = null

    /**
     * @since v465
     */
    var engineVersion: String? = null

    /**
     * @since v485
     */
    var identifier: String? = null

    /**
     * @since v485
     */
    var instanceName: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        id = byteBuf.readUnsignedVarInt()
        data = byteBuf.readTag()
        engineVersion = byteBuf.readString()
        identifier = byteBuf.readString()
        instanceName = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(Integer.toUnsignedLong(id).toInt())
        byteBuf.writeTag(data!!)
        byteBuf.writeString(engineVersion!!)
        byteBuf.writeString(identifier!!)
        byteBuf.writeString(instanceName!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADD_VOLUME_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
