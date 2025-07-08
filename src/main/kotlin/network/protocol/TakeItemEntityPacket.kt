package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.DataPacket
import org.chorus_oss.chorus.network.PacketDecoder
import org.chorus_oss.chorus.network.PacketHandler
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class TakeItemEntityPacket : DataPacket() {
    @JvmField
    var entityId: Long = 0

    @JvmField
    var target: Long = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorRuntimeID(this.target)
        byteBuf.writeActorRuntimeID(this.entityId)
    }

    override fun pid(): Int {
        return ProtocolInfo.TAKE_ITEM_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<TakeItemEntityPacket> {
        override fun decode(byteBuf: HandleByteBuf): TakeItemEntityPacket {
            val packet = TakeItemEntityPacket()

            packet.target = byteBuf.readActorRuntimeID()
            packet.entityId = byteBuf.readActorRuntimeID()

            return packet
        }
    }
}
