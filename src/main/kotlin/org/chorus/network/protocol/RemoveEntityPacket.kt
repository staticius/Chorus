package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class RemoveEntityPacket : DataPacket() {
    @JvmField
    var eid: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.eid)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
