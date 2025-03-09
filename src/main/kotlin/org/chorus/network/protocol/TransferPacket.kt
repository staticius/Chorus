package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class TransferPacket : DataPacket() {
    @JvmField
    var address: String? = null
    @JvmField
    var port: Int = 19132
    private var reloadWorld = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.address = byteBuf.readString()
        this.port = byteBuf.readShortLE().toInt()
        this.reloadWorld = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(address!!)
        byteBuf.writeShortLE(port)
        byteBuf.writeBoolean(this.reloadWorld)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TRANSFER_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
