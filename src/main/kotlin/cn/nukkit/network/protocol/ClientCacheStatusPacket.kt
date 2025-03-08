package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ClientCacheStatusPacket : DataPacket() {
    var supported: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.supported = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.supported)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CLIENT_CACHE_STATUS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
