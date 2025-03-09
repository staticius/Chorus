package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AddPaintingPacket : DataPacket() {
    var entityUniqueId: Long = 0
    var entityRuntimeId: Long = 0
    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f
    var direction: Int = 0
    var title: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.entityUniqueId)
        byteBuf.writeEntityRuntimeId(this.entityRuntimeId)

        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeVarInt(this.direction)
        byteBuf.writeString(title!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADD_PAINTING_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
