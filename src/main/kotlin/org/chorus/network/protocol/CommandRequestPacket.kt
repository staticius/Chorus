package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.CommandOriginData


class CommandRequestPacket(
    var command: String,
    var commandOrigin: CommandOriginData,
    var isInternalSource: Boolean,
    var version: Int,
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
