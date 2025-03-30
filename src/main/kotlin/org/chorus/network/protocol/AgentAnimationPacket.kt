package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorRuntimeID

data class AgentAnimationPacket(
    val animation: Byte,
    val runtimeID: ActorRuntimeID,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(animation.toInt())
        byteBuf.writeActorRuntimeID(this.runtimeID)
    }

    override fun pid(): Int {
        return ProtocolInfo.AGENT_ANIMATION
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
