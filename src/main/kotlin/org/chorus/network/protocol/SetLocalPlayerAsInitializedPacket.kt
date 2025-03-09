package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetLocalPlayerAsInitializedPacket : DataPacket() {
    var eid: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        eid = byteBuf.readUnsignedVarLong()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarLong(eid)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
