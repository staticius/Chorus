package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.math.Vector3f
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class ToggleCrafterSlotRequestPacket : DataPacket() {
    var blockPosition: Vector3f? = null
    var slot: Byte = 0
    var disabled: Boolean = false

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

    companion object : PacketDecoder<ToggleCrafterSlotRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): ToggleCrafterSlotRequestPacket {
            val packet = ToggleCrafterSlotRequestPacket()

            packet.blockPosition = byteBuf.readVector3f()
            packet.slot = byteBuf.readByte()
            packet.disabled = byteBuf.readBoolean()

            return packet
        }
    }
}
