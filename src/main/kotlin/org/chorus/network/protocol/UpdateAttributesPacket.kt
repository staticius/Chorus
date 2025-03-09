package org.chorus.network.protocol

import org.chorus.entity.Attribute
import org.chorus.network.connection.util.HandleByteBuf
import lombok.*






class UpdateAttributesPacket : DataPacket() {
    @JvmField
    var entries: Array<Attribute>?
    @JvmField
    var entityId: Long = 0
    var frame: Long = 0 //tick

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(this.entityId)

        if (this.entries == null) {
            byteBuf.writeUnsignedVarInt(0)
        } else {
            byteBuf.writeUnsignedVarInt(entries!!.size)
            for (entry in entries!!) {
                byteBuf.writeFloatLE(entry.getMinValue())
                byteBuf.writeFloatLE(entry.getMaxValue())
                byteBuf.writeFloatLE(entry.getValue())
                byteBuf.writeFloatLE(entry.getDefaultMinimum())
                byteBuf.writeFloatLE(entry.getDefaultMaximum())
                byteBuf.writeFloatLE(entry.getDefaultValue())
                byteBuf.writeString(entry.getName())
                byteBuf.writeUnsignedVarInt(0) // Modifiers
            }
        }
        byteBuf.writeUnsignedVarInt(frame.toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_ATTRIBUTES_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
