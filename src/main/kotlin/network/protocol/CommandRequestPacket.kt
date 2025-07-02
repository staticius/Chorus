package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.CommandOriginData

data class CommandRequestPacket(
    val command: String,
    val commandOrigin: CommandOriginData,
    val isInternalSource: Boolean,
    val version: Int,
) : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.COMMAND_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<CommandRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): CommandRequestPacket {
            return CommandRequestPacket(
                command = byteBuf.readString(),
                commandOrigin = byteBuf.readCommandOriginData(),
                isInternalSource = byteBuf.readBoolean(),
                version = byteBuf.readVarInt()
            )
        }
    }
}
