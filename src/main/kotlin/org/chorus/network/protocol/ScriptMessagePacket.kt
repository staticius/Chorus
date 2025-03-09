package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class ScriptMessagePacket : DataPacket() {
    private var channel: String? = null
    var message: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.channel = byteBuf.readString()
        this.message = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(channel!!)
        byteBuf.writeString(message!!)
    }

    fun _getChannel(): String? {
        return channel
    }

    fun setChannel(channel: String?) {
        this.channel = channel
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SCRIPT_MESSAGE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
