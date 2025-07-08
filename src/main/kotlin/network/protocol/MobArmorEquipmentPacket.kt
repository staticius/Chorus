package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.DataPacket
import org.chorus_oss.chorus.network.PacketDecoder
import org.chorus_oss.chorus.network.PacketHandler
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class MobArmorEquipmentPacket : DataPacket() {
    var eid: Long = 0
    lateinit var slots: Array<Item>
    var body: Item = Item.AIR

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorRuntimeID(this.eid)
        byteBuf.writeSlot(slots[0])
        byteBuf.writeSlot(slots[1])
        byteBuf.writeSlot(slots[2])
        byteBuf.writeSlot(slots[3])
        byteBuf.writeSlot(this.body)
    }

    override fun pid(): Int {
        return ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<MobArmorEquipmentPacket> {
        override fun decode(byteBuf: HandleByteBuf): MobArmorEquipmentPacket {
            val packet = MobArmorEquipmentPacket()
            packet.eid = byteBuf.readActorRuntimeID()
            packet.slots = Array(4) {
                byteBuf.readSlot()
            }
            packet.body = byteBuf.readSlot()
            return packet
        }
    }
}
