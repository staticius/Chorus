package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class ServerSettingsResponsePacket : DataPacket() {
    @JvmField
    var formId: Int = 0

    @JvmField
    var data: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.formId)
        byteBuf.writeString(data!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SERVER_SETTINGS_RESPONSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
