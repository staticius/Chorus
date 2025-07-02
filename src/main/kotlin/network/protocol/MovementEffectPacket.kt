package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.MovementEffectType


class MovementEffectPacket : DataPacket() {
    var targetRuntimeID: Long = 0
    lateinit var effectType: MovementEffectType
    var effectDuration: Int = 0
    var tick: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarLong(this.targetRuntimeID)
        byteBuf.writeUnsignedVarInt(this.effectType.id)
        byteBuf.writeUnsignedVarInt(this.effectDuration)
        byteBuf.writeUnsignedVarLong(this.tick)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MOVEMENT_EFFECT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
