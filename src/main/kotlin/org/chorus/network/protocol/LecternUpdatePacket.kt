package org.chorus.network.protocol

import cn.nukkit.math.BlockVector3
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class LecternUpdatePacket : DataPacket() {
    var page: Int = 0
    var totalPages: Int = 0
    var blockPosition: BlockVector3? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.page = byteBuf.readUnsignedByte().toInt()
        this.totalPages = byteBuf.readUnsignedByte().toInt()
        this.blockPosition = byteBuf.readBlockVector3()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.LECTERN_UPDATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
