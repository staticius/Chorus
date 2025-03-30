package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.AgentActionType

data class AgentActionEventPacket(
    val requestId: String,
    val action: AgentActionType,
    val response: String,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(requestId)
        byteBuf.writeInt(action.ordinal)
        byteBuf.writeString(response)
    }

    override fun pid(): Int {
        return ProtocolInfo.AGENT_ACTION_EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
