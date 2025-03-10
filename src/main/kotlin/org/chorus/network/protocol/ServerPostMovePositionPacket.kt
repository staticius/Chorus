package org.chorus.network.protocol

import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf







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
