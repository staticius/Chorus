package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class GUIDataPickItemPacket : DataPacket() {
    var hotbarSlot: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.hotbarSlot = byteBuf.readIntLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeIntLE(this.hotbarSlot)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.GUI_DATA_PICK_ITEM_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
