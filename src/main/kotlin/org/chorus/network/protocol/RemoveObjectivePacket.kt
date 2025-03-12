package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class RemoveObjectivePacket : DataPacket() {
    @JvmField
    var objectiveName: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(objectiveName!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REMOVE_OBJECTIVE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
