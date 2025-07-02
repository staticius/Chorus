package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.utils.MainLogger


class PacketViolationWarningPacket : DataPacket() {
    lateinit var type: PacketViolationType
    lateinit var severity: PacketViolationSeverity
    var packetId: Int = 0
    lateinit var context: String

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.type.ordinal - 1)
        byteBuf.writeVarInt(this.severity.ordinal)
        byteBuf.writeVarInt(this.packetId)
        byteBuf.writeString(this.context)
    }

    enum class PacketViolationType {
        UNKNOWN,
        MALFORMED_PACKET
    }

    enum class PacketViolationSeverity {
        UNKNOWN,
        WARNING,
        FINAL_WARNING,
        TERMINATING_CONNECTION
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PACKET_VIOLATION_WARNING_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<PacketViolationWarningPacket> {
        override fun decode(byteBuf: HandleByteBuf): PacketViolationWarningPacket {
            val packet = PacketViolationWarningPacket()

            packet.type = PacketViolationType.entries[byteBuf.readVarInt() + 1]
            packet.severity = PacketViolationSeverity.entries[byteBuf.readVarInt()]
            packet.packetId = byteBuf.readVarInt()
            packet.context = byteBuf.readString()

            MainLogger.log.warn("Packet violation warning: {}", packet)

            return packet
        }
    }
}
