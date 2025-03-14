package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class TickingAreasLoadStatusPacket : DataPacket() {
    var waitingForPreload: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.waitingForPreload)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TICKING_AREAS_LOAD_STATUS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
