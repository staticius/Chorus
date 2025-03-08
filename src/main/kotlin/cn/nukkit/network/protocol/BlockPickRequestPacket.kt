package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class BlockPickRequestPacket : DataPacket() {
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var addUserData: Boolean = false
    var selectedSlot: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        val v = byteBuf.readSignedBlockPosition()
        this.x = v.x
        this.y = v.y
        this.z = v.z
        this.addUserData = byteBuf.readBoolean()
        this.selectedSlot = byteBuf.readByte().toInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.BLOCK_PICK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
