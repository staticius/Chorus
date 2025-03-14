package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class MapCreateLockedCopyPacket : DataPacket() {
    var originalMapId: Long = 0
    var newMapId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.originalMapId = byteBuf.readVarLong()
        this.newMapId = byteBuf.readVarLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarLong(this.originalMapId)
        byteBuf.writeVarLong(this.newMapId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MAP_CREATE_LOCKED_COPY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
