package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author joserobjr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PositionTrackingDBClientRequestPacket : DataPacket() {
    var action: Action? = null
    var trackingId: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        val aByte = byteBuf.readByte().toInt()
        action = ACTIONS[aByte]
        trackingId = byteBuf.readVarInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(action!!.ordinal().toByte().toInt())
        byteBuf.writeVarInt(trackingId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.POS_TRACKING_CLIENT_REQUEST_PACKET
    }

    enum class Action {
        QUERY
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        private val ACTIONS = Action.entries.toTypedArray()
    }
}
