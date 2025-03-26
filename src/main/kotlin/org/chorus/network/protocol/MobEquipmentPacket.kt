package org.chorus.network.protocol

import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf


class MobEquipmentPacket : DataPacket() {
    var eid: Long = 0
    lateinit var item: Item
    var slot: Int = 0
    var selectedSlot: Int = 0
    var containerId: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.eid = byteBuf.readEntityRuntimeId() //EntityRuntimeID
        this.item = byteBuf.readSlot()
        this.slot = byteBuf.readByte().toInt()
        this.selectedSlot = byteBuf.readByte().toInt()
        this.containerId = byteBuf.readByte().toInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.eid) //EntityRuntimeID
        byteBuf.writeSlot(this.item)
        byteBuf.writeByte(slot.toByte().toInt())
        byteBuf.writeByte(selectedSlot.toByte().toInt())
        byteBuf.writeByte(containerId.toByte().toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MOB_EQUIPMENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
