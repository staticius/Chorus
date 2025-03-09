package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RequestChunkRadiusPacket : DataPacket() {
    var radius: Int = 0

    /**
     * @since v582
     */
    private var maxRadius = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.radius = byteBuf.readVarInt()
        this.maxRadius = byteBuf.readByte().toInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REQUEST_CHUNK_RADIUS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
