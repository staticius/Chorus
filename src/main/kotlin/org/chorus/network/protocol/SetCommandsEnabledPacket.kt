package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class SetCommandsEnabledPacket : DataPacket() {
    var enabled: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.enabled)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_COMMANDS_ENABLED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
