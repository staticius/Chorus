package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RequestNetworkSettingsPacket : DataPacket() {
    var protocolVersion: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.protocolVersion = byteBuf.readInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        throw UnsupportedOperationException()
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REQUEST_NETWORK_SETTINGS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
