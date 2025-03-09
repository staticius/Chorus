package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class SimpleEventPacket : DataPacket() {
    var type: Short = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeShort(type.toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SIMPLE_EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
