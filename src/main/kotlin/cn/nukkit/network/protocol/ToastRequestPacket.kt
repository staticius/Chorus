package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ToastRequestPacket : DataPacket() {
    @JvmField
    var title: String = ""
    @JvmField
    var content: String = ""

    override fun decode(byteBuf: HandleByteBuf) {
        this.title = byteBuf.readString()
        this.content = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(this.title)
        byteBuf.writeString(this.content)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TOAST_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
