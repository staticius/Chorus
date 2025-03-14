package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


//EDU exclusive


class CodeBuilderPacket : DataPacket() {
    var isOpening: Boolean = false
    var url: String = ""

    override fun decode(byteBuf: HandleByteBuf) {
        this.url = byteBuf.readString()
        this.isOpening = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(url)
        byteBuf.writeBoolean(isOpening)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CODE_BUILDER_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
