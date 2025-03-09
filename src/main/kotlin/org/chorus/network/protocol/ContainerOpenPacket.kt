package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ContainerOpenPacket : DataPacket() {
    var windowId: Int = 0
    var type: Int = 0
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var entityId: Long = -1
    override fun decode(byteBuf: HandleByteBuf) {
        this.windowId = byteBuf.readByte().toInt()
        this.type = byteBuf.readByte().toInt()

        val v = byteBuf.readBlockVector3()
        this.x = v.x
        this.y = v.y
        this.z = v.z

        this.entityId = byteBuf.readEntityUniqueId()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(windowId.toByte().toInt())
        byteBuf.writeByte(type.toByte().toInt())
        byteBuf.writeBlockVector3(this.x, this.y, this.z)
        byteBuf.writeEntityUniqueId(this.entityId)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CONTAINER_OPEN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
