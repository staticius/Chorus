package org.chorus.network.protocol

import cn.nukkit.math.Vector3f
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ServerPostMovePositionPacket : DataPacket() {
    var position: Vector3f? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.position = byteBuf.readVector3f()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVector3f(position!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SERVER_POST_MOVE_POSITION
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
