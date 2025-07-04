package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetLocalPlayerAsInitializedPacket : DataPacket() {
    var eid: Long = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarLong(eid)
    }

    override fun pid(): Int {
        return ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SetLocalPlayerAsInitializedPacket> {
        override fun decode(byteBuf: HandleByteBuf): SetLocalPlayerAsInitializedPacket {
            val packet = SetLocalPlayerAsInitializedPacket()

            packet.eid = byteBuf.readUnsignedVarLong()

            return packet
        }
    }
}
