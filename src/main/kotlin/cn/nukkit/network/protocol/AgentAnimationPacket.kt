package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AgentAnimationPacket : DataPacket() {
    var animation: Byte = 0
    var runtimeEntityId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.animation = byteBuf.readByte()
        this.runtimeEntityId = byteBuf.readEntityRuntimeId()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(animation.toInt())
        byteBuf.writeEntityRuntimeId(this.runtimeEntityId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.AGENT_ANIMATION
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
