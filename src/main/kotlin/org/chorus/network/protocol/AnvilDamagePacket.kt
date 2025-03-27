package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class AnvilDamagePacket(
    var damage: Int,
    var x: Int,
    var y: Int,
    var z: Int,
) : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.ANVIL_DAMAGE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<AnvilDamagePacket> {
        override fun decode(byteBuf: HandleByteBuf): AnvilDamagePacket {
            val damage = byteBuf.readByte().toInt()
            val vec = byteBuf.readBlockVector3()

            return AnvilDamagePacket(
                damage,
                x = vec.x,
                y = vec.y,
                z = vec.z,
            )
        }
    }
}
