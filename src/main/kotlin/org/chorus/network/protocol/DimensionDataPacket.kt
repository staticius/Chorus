package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*


@Getter
@Setter
@ToString
@NoArgsConstructor
class DimensionDataPacket : DataPacket() {
    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DIMENSION_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
