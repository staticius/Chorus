package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.CommandOriginData
import org.chorus.network.protocol.types.CommandOutputMessage
import org.chorus.network.protocol.types.CommandOutputType

class CommandOutputPacket : DataPacket() {
    @JvmField
    val messages: MutableList<CommandOutputMessage> = mutableListOf()

    @JvmField
    var commandOriginData: CommandOriginData? = null

    @JvmField
    var type: CommandOutputType? = null

    @JvmField
    var successCount: Int = 0
    var data: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(commandOriginData!!.type.ordinal)
        byteBuf.writeUUID(commandOriginData!!.uuid)
        byteBuf.writeString(commandOriginData!!.requestId) // unknown
        if (commandOriginData!!.type == CommandOriginData.Origin.DEV_CONSOLE || commandOriginData!!.type == CommandOriginData.Origin.TEST) {
            byteBuf.writeVarLong(commandOriginData!!.varLong.orElse(-1)) // unknown
        }

        byteBuf.writeByte(type!!.ordinal.toByte().toInt())
        byteBuf.writeUnsignedVarInt(this.successCount)

        byteBuf.writeUnsignedVarInt(messages.size)
        for (msg in messages) {
            byteBuf.writeBoolean(msg.internal)
            byteBuf.writeString(msg.messageId)
            byteBuf.writeUnsignedVarInt(msg.parameters.size)
            for (param in msg.parameters) {
                byteBuf.writeString(param)
            }
        }
        if (this.type == CommandOutputType.DATA_SET) {
            byteBuf.writeString(data!!) // unknown
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.COMMAND_OUTPUT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
