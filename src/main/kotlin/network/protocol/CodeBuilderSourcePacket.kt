package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.CodeBuilderCategoryType
import org.chorus_oss.chorus.network.protocol.types.CodeBuilderCodeStatus
import org.chorus_oss.chorus.network.protocol.types.CodeBuilderOperationType

data class CodeBuilderSourcePacket(
    val operation: CodeBuilderOperationType,
    val category: CodeBuilderCategoryType,
    val codeStatus: CodeBuilderCodeStatus,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(operation.ordinal)
        byteBuf.writeByte(category.ordinal)
        byteBuf.writeByte(codeStatus.ordinal)
    }

    override fun pid(): Int {
        return ProtocolInfo.CODE_BUILDER_SOURCE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<CodeBuilderSourcePacket> {
        override fun decode(byteBuf: HandleByteBuf): CodeBuilderSourcePacket {
            return CodeBuilderSourcePacket(
                operation = CodeBuilderOperationType.entries[byteBuf.readByte().toInt()],
                category = CodeBuilderCategoryType.entries[byteBuf.readByte().toInt()],
                codeStatus = CodeBuilderCodeStatus.entries[byteBuf.readByte().toInt()],
            )
        }
    }
}
