package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf

class MapInfoRequestPacket : DataPacket() {
    var mapId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        mapId = byteBuf.readActorUniqueID()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MAP_INFO_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
