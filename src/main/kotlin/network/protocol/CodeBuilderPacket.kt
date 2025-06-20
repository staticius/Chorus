package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class CodeBuilderPacket(
    val url: String,
    val shouldOpenCodeBuilder: Boolean,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(url)
        byteBuf.writeBoolean(shouldOpenCodeBuilder)
    }

    override fun pid(): Int {
        return ProtocolInfo.CODE_BUILDER_PACKET
    }
}
