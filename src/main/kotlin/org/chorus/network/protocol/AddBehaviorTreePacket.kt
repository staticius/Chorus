package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


data class AddBehaviorTreePacket(
    val behaviorTreeJSON: String
) : DataPacket(), PacketEncoder {
    override fun decode(byteBuf: HandleByteBuf) {}

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(behaviorTreeJSON)
    }

    override fun pid(): Int {
        return ProtocolInfo.ADD_BEHAVIOR_TREE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
