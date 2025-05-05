package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class MapCreateLockedCopyPacket : DataPacket() {
    var originalMapId: Long = 0
    var newMapId: Long = 0

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

    companion object : PacketDecoder<MapCreateLockedCopyPacket> {
        override fun decode(byteBuf: HandleByteBuf): MapCreateLockedCopyPacket {
            val packet = MapCreateLockedCopyPacket()

            packet.originalMapId = byteBuf.readVarLong()
            packet.newMapId = byteBuf.readVarLong()

            return packet
        }
    }
}
