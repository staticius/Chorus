package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ShowCreditsPacket : DataPacket() {
    @JvmField
    var eid: Long = 0

    @JvmField
    var status: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorRuntimeID(this.eid)
        byteBuf.writeVarInt(this.status)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SHOW_CREDITS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ShowCreditsPacket> {
        override fun decode(byteBuf: HandleByteBuf): ShowCreditsPacket {
            val packet = ShowCreditsPacket()

            packet.eid = byteBuf.readActorRuntimeID()
            packet.status = byteBuf.readVarInt()

            return packet
        }

        const val STATUS_START_CREDITS: Int = 0
        const val STATUS_END_CREDITS: Int = 1
    }
}
