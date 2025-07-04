package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class PositionTrackingDBClientRequestPacket : DataPacket() {
    var action: Action? = null
    var trackingId: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(action!!.ordinal.toByte().toInt())
        byteBuf.writeVarInt(trackingId)
    }

    override fun pid(): Int {
        return ProtocolInfo.POS_TRACKING_CLIENT_REQUEST_PACKET
    }

    enum class Action {
        QUERY
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<PositionTrackingDBClientRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): PositionTrackingDBClientRequestPacket {
            val packet = PositionTrackingDBClientRequestPacket()

            packet.action = ACTIONS[byteBuf.readByte().toInt()]
            packet.trackingId = byteBuf.readVarInt()

            return packet
        }

        private val ACTIONS = Action.entries.toTypedArray()
    }
}
