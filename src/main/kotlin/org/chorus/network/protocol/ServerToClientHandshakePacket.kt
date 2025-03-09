package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ServerToClientHandshakePacket : DataPacket() {
    var jwt: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(jwt!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SERVER_TO_CLIENT_HANDSHAKE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
