package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class MobEffectPacket : DataPacket() {
    var eid: Long = 0

    var eventId: Int = 0
    var effectId: Int = 0
    var amplifier: Int = 0
    var particles: Boolean = true
    var duration: Int = 0

    /**
     * @since v662
     */
    var tick: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.eid)
        byteBuf.writeByte(eventId.toByte().toInt())
        byteBuf.writeVarInt(this.effectId)
        byteBuf.writeVarInt(this.amplifier)
        byteBuf.writeBoolean(this.particles)
        byteBuf.writeVarInt(this.duration)
        byteBuf.writeUnsignedVarLong(this.tick)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MOB_EFFECT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val EVENT_ADD: Byte = 1
        const val EVENT_MODIFY: Byte = 2
        const val EVENT_REMOVE: Byte = 3
    }
}
