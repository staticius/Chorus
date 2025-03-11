package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf

import java.util.*





class PlayerArmorDamagePacket : DataPacket() {
    val flags: MutableSet<PlayerArmorDamageFlag> = EnumSet.noneOf(
        PlayerArmorDamageFlag::class.java
    )
    val damage: IntArray = IntArray(5)

    override fun decode(byteBuf: HandleByteBuf) {
        val flagsval = byteBuf.readByte().toInt()
        for (i in 0..4) {
            if ((flagsval and (1 shl i)) != 0) {
                flags.add(PlayerArmorDamageFlag.entries[i])
                damage[i] = byteBuf.readVarInt()
            }
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        var outflags = 0
        for (flag in this.flags) {
            outflags = outflags or (1 shl flag.ordinal())
        }
        byteBuf.writeByte(outflags)

        for (flag in this.flags) {
            byteBuf.writeVarInt(damage[flag.ordinal()])
        }
    }

    enum class PlayerArmorDamageFlag {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        BODY
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_ARMOR_DAMAGE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
