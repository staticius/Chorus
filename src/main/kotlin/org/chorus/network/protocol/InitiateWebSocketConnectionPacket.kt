package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf






class InitiateWebSocketConnectionPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        //TODO: Implement
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.INITIATE_WEB_SOCKET_CONNECTION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
