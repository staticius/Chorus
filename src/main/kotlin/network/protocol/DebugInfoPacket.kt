package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class DebugInfoPacket : DataPacket() {
    var entityId: Long = 0
    var data: String? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLong(this.entityId)
        byteBuf.writeString(data!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DEBUG_INFO_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<DebugInfoPacket> {
        override fun decode(byteBuf: HandleByteBuf): DebugInfoPacket {
            val packet = DebugInfoPacket()

            packet.entityId = byteBuf.readLong()
            packet.data = byteBuf.readString()

            return packet
        }
    }
}
