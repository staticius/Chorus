package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RemoveObjectivePacket : DataPacket() {
    @JvmField
    var objectiveName: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(objectiveName!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_OBJECTIVE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
