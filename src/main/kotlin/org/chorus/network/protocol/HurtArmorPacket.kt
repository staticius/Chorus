package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class HurtArmorPacket : DataPacket() {
    var cause: Int = 0
    var damage: Int = 0
    var armorSlots: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.cause = byteBuf.readVarInt()
        this.damage = byteBuf.readVarInt()
        this.armorSlots = byteBuf.readUnsignedVarLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.cause)
        byteBuf.writeVarInt(this.damage)
        byteBuf.writeUnsignedVarLong(this.armorSlots)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.HURT_ARMOR_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
