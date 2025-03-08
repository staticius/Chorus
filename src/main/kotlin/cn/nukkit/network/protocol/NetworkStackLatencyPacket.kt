package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
