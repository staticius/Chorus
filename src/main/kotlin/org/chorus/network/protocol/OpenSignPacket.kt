package org.chorus.network.protocol

import cn.nukkit.math.BlockVector3
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
@Builder
@Getter
@Setter
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
class OpenSignPacket : DataPacket() {
    @JvmField
    var position: BlockVector3? = null
    @JvmField
    var frontSide: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.position = byteBuf.readBlockVector3()
        this.frontSide = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(position!!)
        byteBuf.writeBoolean(frontSide)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.OPEN_SIGN
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
