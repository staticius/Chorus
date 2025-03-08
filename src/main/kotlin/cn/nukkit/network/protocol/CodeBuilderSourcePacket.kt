package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.CodeBuilderCategoryType
import cn.nukkit.network.protocol.types.CodeBuilderOperationType
import lombok.*

//EDU exclusive
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class CodeBuilderSourcePacket : DataPacket() {
    var operation: CodeBuilderOperationType? = null
    var category: CodeBuilderCategoryType? = null
    var value: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.operation = CodeBuilderOperationType.entries[byteBuf.readByte().toInt()]
        this.category = CodeBuilderCategoryType.entries[byteBuf.readByte().toInt()]
        this.value = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(operation!!.ordinal().toByte().toInt())
        byteBuf.writeByte(category!!.ordinal().toByte().toInt())
        byteBuf.writeString(value!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CODE_BUILDER_SOURCE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
