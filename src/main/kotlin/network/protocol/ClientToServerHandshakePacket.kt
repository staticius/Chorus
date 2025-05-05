package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class ClientToServerHandshakePacket : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ClientToServerHandshakePacket> {
        override fun decode(byteBuf: HandleByteBuf): ClientToServerHandshakePacket {
            return ClientToServerHandshakePacket()
        }
    }
}
