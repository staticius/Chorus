package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.math.Vector3f
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ServerPostMovePositionPacket : DataPacket() {
    lateinit var position: Vector3f

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVector3f(position)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SERVER_POST_MOVE_POSITION
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
