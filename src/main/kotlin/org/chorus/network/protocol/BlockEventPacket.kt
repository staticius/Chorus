package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class BlockEventPacket : DataPacket() {
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var type: Int = 0
    @JvmField
    var value: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(this.x, this.y, this.z)
        byteBuf.writeVarInt(this.type)
        byteBuf.writeVarInt(this.value)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.BLOCK_EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
