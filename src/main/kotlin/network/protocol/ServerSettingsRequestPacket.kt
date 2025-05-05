package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class ServerSettingsRequestPacket : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ServerSettingsRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): ServerSettingsRequestPacket {
            return ServerSettingsRequestPacket()
        }
    }
}
