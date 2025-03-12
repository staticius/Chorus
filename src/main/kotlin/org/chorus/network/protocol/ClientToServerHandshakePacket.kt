package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class ClientToServerHandshakePacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CLIENT_TO_SERVER_HANDSHAKE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
