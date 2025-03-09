package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.AgentActionType
import lombok.*

/**
 * @since v503
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AgentActionEventPacket : DataPacket() {
    var requestId: String? = null
    var actionType: AgentActionType? = null

    /**
     * @see AgentActionType for type specific JSON
     */
    var responseJson: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.requestId = byteBuf.readString()
        this.actionType = AgentActionType.entries[byteBuf.readByte().toInt()]
        this.responseJson = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(requestId!!)
        byteBuf.writeByte(actionType!!.ordinal.toByte().toInt())
        byteBuf.writeString(responseJson!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.AGENT_ACTION_EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
