package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class HurtArmorPacket(
    val cause: Int,
    val damage: Int,
    val armorSlots: Long,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.cause)
        byteBuf.writeVarInt(this.damage)
        byteBuf.writeUnsignedVarLong(this.armorSlots)
    }

    override fun pid(): Int {
        return ProtocolInfo.HURT_ARMOR_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<HurtArmorPacket> {
        override fun decode(byteBuf: HandleByteBuf): HurtArmorPacket {
            return HurtArmorPacket(
                cause = byteBuf.readVarInt(),
                damage = byteBuf.readVarInt(),
                armorSlots = byteBuf.readUnsignedVarLong()
            )
        }
    }
}
