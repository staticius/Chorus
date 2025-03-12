package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.MovementEffectType


class MovementEffectPacket : DataPacket() {
    var targetRuntimeID: Long = 0
    var effectType: MovementEffectType? = null
    var effectDuration: Int = 0
    var tick: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarLong(this.targetRuntimeID)
        byteBuf.writeUnsignedVarInt(effectType.getId())
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
