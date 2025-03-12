package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class AnvilDamagePacket : DataPacket() {
    var damage: Int = 0
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.damage = byteBuf.readByte().toInt()
        val vec = byteBuf.readBlockVector3()
        this.x = vec.x
        this.y = vec.y
        this.z = vec.z
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ANVIL_DAMAGE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
