package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class NetworkStackLatencyPacket : DataPacket() {
    var timestamp: Long = 0
    var unknownBool: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        timestamp = byteBuf.readLongLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLongLE(timestamp)
        byteBuf.writeBoolean(unknownBool)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.NETWORK_STACK_LATENCY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
