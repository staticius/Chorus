package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.DataPacket
import org.chorus_oss.chorus.network.PacketDecoder
import org.chorus_oss.chorus.network.PacketHandler
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


class MobEquipmentPacket : DataPacket() {
    var eid: Long = 0
    lateinit var item: Item
    var slot: Int = 0
    var selectedSlot: Int = 0
    var containerId: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorRuntimeID(this.eid)
        byteBuf.writeSlot(this.item)
        byteBuf.writeByte(slot)
        byteBuf.writeByte(selectedSlot)
        byteBuf.writeByte(containerId)
    }

    override fun pid(): Int {
        return ProtocolInfo.MOB_EQUIPMENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<MobEquipmentPacket> {
        override fun decode(byteBuf: HandleByteBuf): MobEquipmentPacket {
            val packet = MobEquipmentPacket()
            packet.eid = byteBuf.readActorRuntimeID()
            packet.item = byteBuf.readSlot()
            packet.slot = byteBuf.readByte().toInt()
            packet.selectedSlot = byteBuf.readByte().toInt()
            packet.containerId = byteBuf.readByte().toInt()
            return packet
        }
    }
}
