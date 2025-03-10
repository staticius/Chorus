package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class DebugInfoPacket : DataPacket() {
    var entityId: Long = 0
    var data: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.entityId = byteBuf.readLong()
        this.data = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLong(this.entityId)
        byteBuf.writeString(data!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DEBUG_INFO_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
