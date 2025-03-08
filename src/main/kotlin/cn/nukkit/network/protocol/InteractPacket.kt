package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @since 15-10-15
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class InteractPacket : DataPacket() {
    var action: Int = 0
    var target: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.action = byteBuf.readByte().toInt()
        this.target = byteBuf.readEntityRuntimeId()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(action.toByte().toInt())
        byteBuf.writeEntityRuntimeId(this.target)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.INTERACT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val ACTION_VEHICLE_EXIT: Int = 3
        const val ACTION_MOUSEOVER: Int = 4
        const val ACTION_OPEN_INVENTORY: Int = 6
    }
}
