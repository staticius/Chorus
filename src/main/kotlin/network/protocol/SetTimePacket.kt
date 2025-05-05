package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetTimePacket : DataPacket() {
    @JvmField
    var time: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.time)
    }

    override fun pid(): Int {
        return ProtocolInfo.SET_TIME_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
