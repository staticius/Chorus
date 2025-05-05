package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ServerSettingsResponsePacket : DataPacket() {
    @JvmField
    var formId: Int = 0

    @JvmField
    var data: String? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.formId)
        byteBuf.writeString(data!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
