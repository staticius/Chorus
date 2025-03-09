package org.chorus.network.connection.netty.codec.packet

import org.chorus.network.connection.netty.BedrockPacketWrapper
import io.netty.buffer.ByteBuf

class BedrockPacketCodec_v1 : BedrockPacketCodec() {
    override fun encodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper) {
        buf.writeByte(msg.packetId and 0xff)
    }

    override fun decodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper) {
        msg.packetId = buf.readUnsignedByte().toInt()
    }
}
