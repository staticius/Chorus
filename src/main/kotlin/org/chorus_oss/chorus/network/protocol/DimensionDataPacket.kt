package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class DimensionDataPacket : DataPacket() {
    override fun encode(byteBuf: HandleByteBuf) {
        // TODO
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DIMENSION_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
