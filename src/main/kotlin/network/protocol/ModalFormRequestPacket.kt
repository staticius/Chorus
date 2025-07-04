package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ModalFormRequestPacket : DataPacket() {
    @JvmField
    var formId: Int = 0

    lateinit var data: String

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.formId)
        byteBuf.writeString(this.data)
    }

    override fun pid(): Int {
        return ProtocolInfo.MODAL_FORM_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
