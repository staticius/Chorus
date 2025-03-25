package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.DisconnectFailReason

class DisconnectPacket : DataPacket() {
    var reason: DisconnectFailReason = DisconnectFailReason.UNKNOWN

    @JvmField
    var hideDisconnectionScreen: Boolean = false

    @JvmField
    var message: String = ""
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
            byteBuf.writeString(message)
            byteBuf.writeString(this.filteredMessage)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.DISCONNECT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
