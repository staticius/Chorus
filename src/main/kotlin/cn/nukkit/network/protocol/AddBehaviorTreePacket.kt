package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AddBehaviorTreePacket : DataPacket() {
    var behaviorTreeJson: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(behaviorTreeJson!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADD_BEHAVIOR_TREE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
