package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class SetCommandsEnabledPacket : DataPacket() {
    var enabled: Boolean = false

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
