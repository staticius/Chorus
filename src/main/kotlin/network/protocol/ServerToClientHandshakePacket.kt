package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ServerToClientHandshakePacket : DataPacket() {
    var jwt: String? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(jwt!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
