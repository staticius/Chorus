package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author GoodLucky777
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class TickSyncPacket : DataPacket() {
    var requestTimestamp: Long = 0
    var responseTimestamp: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.requestTimestamp = byteBuf.readLongLE()
        this.responseTimestamp = byteBuf.readLongLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLongLE(this.requestTimestamp)
        byteBuf.writeLongLE(this.responseTimestamp)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TICK_SYNC_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
