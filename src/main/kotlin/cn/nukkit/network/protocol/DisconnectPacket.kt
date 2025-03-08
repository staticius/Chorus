package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.DisconnectFailReason
import lombok.*

/**
 * @since 15-10-12
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class DisconnectPacket : DataPacket() {
    var reason: DisconnectFailReason = DisconnectFailReason.UNKNOWN
    @JvmField
    var hideDisconnectionScreen: Boolean = false
    @JvmField
    var message: String? = null
    private var filteredMessage = ""

    override fun decode(byteBuf: HandleByteBuf) {
        this.reason = DisconnectFailReason.entries[byteBuf.readVarInt()]
        this.hideDisconnectionScreen = byteBuf.readBoolean()
        this.message = byteBuf.readString()
        this.filteredMessage = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(reason.ordinal)
        byteBuf.writeBoolean(this.hideDisconnectionScreen)
        if (!this.hideDisconnectionScreen) {
            byteBuf.writeString(message!!)
            byteBuf.writeString(this.filteredMessage)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DISCONNECT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
