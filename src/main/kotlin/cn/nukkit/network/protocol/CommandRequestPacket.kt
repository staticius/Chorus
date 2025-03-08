package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.CommandOriginData
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class CommandRequestPacket : DataPacket() {
    var command: String? = null
    var data: CommandOriginData? = null
    var internal: Boolean = false

    /**
     * @since v567
     */
    var version: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.command = byteBuf.readString()

        val type = CommandOriginData.Origin.entries[byteBuf.readVarInt()]
        val uuid = byteBuf.readUUID()
        val requestId = byteBuf.readString()
        var varLong: Long? = null
        if (type == CommandOriginData.Origin.DEV_CONSOLE || type == CommandOriginData.Origin.TEST) {
            varLong = byteBuf.readVarLong()
        }
        this.data = CommandOriginData(type, uuid, requestId, varLong)
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.COMMAND_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val TYPE_PLAYER: Int = 0
        const val TYPE_COMMAND_BLOCK: Int = 1
        const val TYPE_MINECART_COMMAND_BLOCK: Int = 2
        const val TYPE_DEV_CONSOLE: Int = 3
        const val TYPE_AUTOMATION_PLAYER: Int = 4
        const val TYPE_CLIENT_AUTOMATION: Int = 5
        const val TYPE_DEDICATED_SERVER: Int = 6
        const val TYPE_ENTITY: Int = 7
        const val TYPE_VIRTUAL: Int = 8
        const val TYPE_GAME_ARGUMENT: Int = 9
        const val TYPE_INTERNAL: Int = 10
    }
}
