package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class TickingAreasLoadStatusPacket : DataPacket() {
    var waitingForPreload: Boolean = false

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.waitingForPreload)
    }

    override fun pid(): Int {
        return ProtocolInfo.TICKING_AREAS_LOAD_STATUS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
