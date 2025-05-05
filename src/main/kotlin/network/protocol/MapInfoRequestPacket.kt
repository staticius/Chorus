package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class MapInfoRequestPacket : DataPacket() {
    var mapId: Long = 0

    override fun pid(): Int {
        return ProtocolInfo.MAP_INFO_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<MapInfoRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): MapInfoRequestPacket {
            val packet = MapInfoRequestPacket()
            packet.mapId = byteBuf.readActorUniqueID()
            return packet
        }
    }
}
