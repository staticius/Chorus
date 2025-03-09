package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author Pub4Game
 * @since 03.07.2016
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ItemFrameDropItemPacket : DataPacket() {
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        val v = byteBuf.readBlockVector3()
        this.z = v.z
        this.y = v.y
        this.x = v.x
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ITEM_FRAME_DROP_ITEM_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
