package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.DisconnectFailReason

class DisconnectPacket : DataPacket() {
    var reason: DisconnectFailReason = DisconnectFailReason.UNKNOWN

    @JvmField
    var hideDisconnectionScreen: Boolean = false

    @JvmField
    var message: String = ""
    private var filteredMessage = ""

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
}
