package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


/**
 * @author CreeperFace
 * @since 5.3.2017
 */


class MapInfoRequestPacket : DataPacket() {
    var mapId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        mapId = byteBuf.readEntityUniqueId()
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
