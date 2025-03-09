package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class ModalFormResponsePacket : DataPacket() {
    var formId: Int = 0
    var data: String = "null"
    var cancelReason: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.formId = byteBuf.readVarInt()
        if (byteBuf.readBoolean()) {
            this.data = byteBuf.readString()
        }
        if (byteBuf.readBoolean()) {
            this.cancelReason = byteBuf.readByte().toInt()
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MODAL_FORM_RESPONSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
