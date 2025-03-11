package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class SettingsCommandPacket : DataPacket() {
    var command: String? = null
    var suppressOutput: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.command = byteBuf.readString()
        this.suppressOutput = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(command!!)
        byteBuf.writeBoolean(suppressOutput)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SETTINGS_COMMAND_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
