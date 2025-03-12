package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.EventData


class EventPacket : DataPacket() {
    var eid: Long = 0
    var usePlayerId: Byte = 0
    var eventData: EventData? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarLong(this.eid)
        byteBuf.writeVarInt(eventData.getType().ordinal)
        byteBuf.writeByte(usePlayerId.toInt())
        eventData!!.write(byteBuf)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
