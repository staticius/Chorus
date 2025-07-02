package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class RemoveVolumeEntityPacket : DataPacket() {
    var id: Long = 0

    var dimension: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(id.toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.REMOVE_VOLUME_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<RemoveVolumeEntityPacket> {
        override fun decode(byteBuf: HandleByteBuf): RemoveVolumeEntityPacket {
            val packet = RemoveVolumeEntityPacket()

            packet.id = byteBuf.readUnsignedVarInt().toLong()

            return packet
        }
    }
}
