package org.chorus.network.connection.netty.codec.packet

import cn.nukkit.network.connection.netty.BedrockPacketWrapper
import io.netty.buffer.ByteBuf

class BedrockPacketCodec_v2 : BedrockPacketCodec() {
    override fun encodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper) {
        buf.writeByte(msg.packetId)
        buf.writeByte(msg.senderSubClientId)
        buf.writeByte(msg.targetSubClientId)
    }

    override fun decodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper) {
        msg.packetId = buf.readUnsignedByte().toInt()
        msg.senderSubClientId = buf.readUnsignedByte().toInt()
        msg.targetSubClientId = buf.readUnsignedByte().toInt()
    }
}
