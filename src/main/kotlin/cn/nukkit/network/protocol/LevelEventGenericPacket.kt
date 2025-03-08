package cn.nukkit.network.protocol

import cn.nukkit.nbt.NBTIO.writeValue
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.connection.util.HandleByteBuf
import io.netty.handler.codec.EncoderException
import lombok.*
import java.io.IOException
import java.nio.ByteOrder

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class LevelEventGenericPacket : DataPacket() {
    var eventId: Int = 0
    var tag: CompoundTag? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(eventId)
        try {
            byteBuf.writeBytes(writeValue(tag!!, ByteOrder.LITTLE_ENDIAN, true)!!)
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
