package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class RemoveActorPacket : DataPacket() {
    @JvmField
    var actorUniqueID: Long = 0

    override fun toString(): String {
        return "RemoveEntityPacket(actorUniqueID=$actorUniqueID)"
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.actorUniqueID)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_ACTOR_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
