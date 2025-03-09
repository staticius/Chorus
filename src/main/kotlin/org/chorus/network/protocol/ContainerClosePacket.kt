package org.chorus.network.protocol

import cn.nukkit.inventory.InventoryType
import cn.nukkit.inventory.InventoryType.Companion.from
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ContainerClosePacket : DataPacket() {
    var windowId: Int = 0
    var wasServerInitiated: Boolean = true

    /**
     * @since v685
     */
    var type: InventoryType? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.windowId = byteBuf.readByte().toInt()
        this.type = from(byteBuf.readByte().toInt())
        this.wasServerInitiated = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(windowId.toByte().toInt())
        byteBuf.writeByte(type!!.networkType.toByte().toInt())
        byteBuf.writeBoolean(this.wasServerInitiated)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CONTAINER_CLOSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
