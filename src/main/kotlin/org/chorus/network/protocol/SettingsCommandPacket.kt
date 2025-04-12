package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf

class SettingsCommandPacket : DataPacket() {
    lateinit var command: String
    var suppressOutput: Boolean = false

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(command)
        byteBuf.writeBoolean(suppressOutput)
    }

    override fun pid(): Int {
        return ProtocolInfo.SETTINGS_COMMAND_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SettingsCommandPacket> {
        override fun decode(byteBuf: HandleByteBuf): SettingsCommandPacket {
            val packet = SettingsCommandPacket()

            packet.command = byteBuf.readString()
            packet.suppressOutput = byteBuf.readBoolean()

            return packet
        }
    }
}
