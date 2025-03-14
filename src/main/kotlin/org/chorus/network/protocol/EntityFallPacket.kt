package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class EntityFallPacket : DataPacket() {
    var eid: Long = 0
    var fallDistance: Float = 0f
    var unknown: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.eid = byteBuf.readEntityRuntimeId()
        this.fallDistance = byteBuf.readFloatLE()
        this.unknown = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ENTITY_FALL_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
