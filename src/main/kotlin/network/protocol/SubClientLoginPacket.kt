package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SubClientLoginPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        //TODO: Implement
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SUB_CLIENT_LOGIN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
