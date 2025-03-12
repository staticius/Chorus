package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class ShowCreditsPacket : DataPacket() {
    @JvmField
    var eid: Long = 0

    @JvmField
    var status: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.eid = byteBuf.readEntityRuntimeId()
        this.status = byteBuf.readVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.eid)
        byteBuf.writeVarInt(this.status)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SHOW_CREDITS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val STATUS_START_CREDITS: Int = 0
        const val STATUS_END_CREDITS: Int = 1
    }
}
