package org.chorus.network.protocol

import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf


class MobArmorEquipmentPacket : DataPacket() {
    var eid: Long = 0
    var slots: Array<Item?> = arrayOfNulls(4)
    var body: Item = Item.AIR

    override fun decode(byteBuf: HandleByteBuf) {
        this.eid = byteBuf.readEntityRuntimeId()
        this.slots = arrayOfNulls(4)
        slots[0] = byteBuf.readSlot()
        slots[1] = byteBuf.readSlot()
        slots[2] = byteBuf.readSlot()
        slots[3] = byteBuf.readSlot()
        this.body = byteBuf.readSlot()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.eid)
        byteBuf.writeSlot(slots[0])
        byteBuf.writeSlot(slots[1])
        byteBuf.writeSlot(slots[2])
        byteBuf.writeSlot(slots[3])
        byteBuf.writeSlot(this.body)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.MOB_ARMOR_EQUIPMENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
