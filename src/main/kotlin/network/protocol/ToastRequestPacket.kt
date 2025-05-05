package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ToastRequestPacket : DataPacket() {
    @JvmField
    var title: String = ""

    @JvmField
    var content: String = ""

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(this.title)
        byteBuf.writeString(this.content)
    }

    override fun pid(): Int {
        return ProtocolInfo.TOAST_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ToastRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): ToastRequestPacket {
            val packet = ToastRequestPacket()

            packet.title = byteBuf.readString()
            packet.content = byteBuf.readString()

            return packet
        }
    }
}
