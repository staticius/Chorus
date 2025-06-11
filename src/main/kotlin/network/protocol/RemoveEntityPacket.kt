package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class RemoveEntityPacket : DataPacket() {
    @JvmField
    var entityRuntimeID: Long = 0

    override fun toString(): String {
        return "RemoveEntityPacket(entityRuntimeID=$entityRuntimeID)"
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.entityRuntimeID)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
