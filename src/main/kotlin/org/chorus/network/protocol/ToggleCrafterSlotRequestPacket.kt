package org.chorus.network.protocol

import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf


class ToggleCrafterSlotRequestPacket : DataPacket() {
    var blockPosition: Vector3f? = null
    var slot: Byte = 0
    var disabled: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.blockPosition = byteBuf.readVector3f()
        this.slot = byteBuf.readByte()
        this.disabled = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVector3f(blockPosition!!)
        byteBuf.writeByte(slot.toInt())
        byteBuf.writeBoolean(this.disabled)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TOGGLE_CRAFTER_SLOT_REQUEST
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
