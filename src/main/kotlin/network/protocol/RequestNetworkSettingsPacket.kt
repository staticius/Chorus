package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class RequestNetworkSettingsPacket : DataPacket() {
    var protocolVersion: Int = 0

    override fun pid(): Int {
        return ProtocolInfo.Companion.REQUEST_NETWORK_SETTINGS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<RequestNetworkSettingsPacket> {
        override fun decode(byteBuf: HandleByteBuf): RequestNetworkSettingsPacket {
            val packet = RequestNetworkSettingsPacket()

            packet.protocolVersion = byteBuf.readInt()

            return packet
        }
    }
}
