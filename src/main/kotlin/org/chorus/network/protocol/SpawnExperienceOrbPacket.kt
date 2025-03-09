package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class SpawnExperienceOrbPacket : DataPacket() {
    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f
    var amount: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeUnsignedVarInt(this.amount)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SPAWN_EXPERIENCE_ORB_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
