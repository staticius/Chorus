package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class PacketViolationWarningPacket : DataPacket() {
    var type: PacketViolationType? = null
    var severity: PacketViolationSeverity? = null
    var packetId: Int = 0
    var context: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.type = PacketViolationType.entries[byteBuf.readVarInt() + 1]
        this.severity = PacketViolationSeverity.entries[byteBuf.readVarInt()]
        this.packetId = byteBuf.readVarInt()
        this.context = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(type!!.ordinal() - 1)
        byteBuf.writeVarInt(severity!!.ordinal())
        byteBuf.writeVarInt(this.packetId)
        byteBuf.writeString(context!!)
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
}
