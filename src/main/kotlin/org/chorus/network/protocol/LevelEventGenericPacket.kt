package org.chorus.network.protocol

import io.netty.handler.codec.EncoderException
import org.chorus.nbt.NBTIO.writeValue
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.connection.util.HandleByteBuf
import java.io.IOException
import java.nio.ByteOrder


class LevelEventGenericPacket : DataPacket() {
    var eventId: Int = 0
    var tag: CompoundTag? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(eventId)
        try {
            byteBuf.writeBytes(writeValue(tag!!, ByteOrder.LITTLE_ENDIAN, true))
        } catch (e: IOException) {
            throw EncoderException(e)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.LEVEL_EVENT_GENERIC_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
