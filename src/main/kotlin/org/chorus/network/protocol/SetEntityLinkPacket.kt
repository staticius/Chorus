package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.EntityLink

class SetEntityLinkPacket : DataPacket() {
    var vehicleUniqueId: Long = 0 //from
    var riderUniqueId: Long = 0 //to
    var type: EntityLink.Type? = null
    var immediate: Byte = 0
    var riderInitiated: Boolean = false
    var vehicleAngularVelocity: Float = 0f

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.vehicleUniqueId)
        byteBuf.writeActorUniqueID(this.riderUniqueId)
        byteBuf.writeByte(type!!.ordinal.toByte().toInt())
        byteBuf.writeByte(immediate.toInt())
        byteBuf.writeBoolean(this.riderInitiated)
        byteBuf.writeFloatLE(this.vehicleAngularVelocity)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_ENTITY_LINK_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
