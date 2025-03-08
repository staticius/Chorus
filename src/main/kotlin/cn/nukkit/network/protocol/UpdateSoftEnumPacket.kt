package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class UpdateSoftEnumPacket : DataPacket() {
    var values: List<String> = listOf()
    var name: String = ""
    var type: Type = Type.SET

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(name)
        byteBuf.writeUnsignedVarInt(values.size)

        for (value in values) {
            byteBuf.writeString(value)
        }
        byteBuf.writeByte(type.ordinal.toByte().toInt())
    }

    enum class Type {
        ADD,
        REMOVE,
        SET
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_SOFT_ENUM_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
