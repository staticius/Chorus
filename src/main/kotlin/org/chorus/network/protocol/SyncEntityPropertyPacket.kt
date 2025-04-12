package org.chorus.network.protocol


import io.netty.buffer.ByteBufInputStream
import org.chorus.nbt.NBTIO.read
import org.chorus.nbt.NBTIO.write
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.Loggable
import java.nio.ByteOrder


class SyncEntityPropertyPacket(
    var data: CompoundTag? = null
) : DataPacket() {
    override fun encode(byteBuf: HandleByteBuf) {
        try {
            byteBuf.writeBytes(write(data!!, ByteOrder.BIG_ENDIAN, true))
        } catch (e: Exception) {
            SyncEntityPropertyPacket.log.error("", e)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.SYNC_ENTITY_PROPERTY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<SyncEntityPropertyPacket>, Loggable {
        override fun decode(byteBuf: HandleByteBuf): SyncEntityPropertyPacket {
            val packet = SyncEntityPropertyPacket()

            try {
                ByteBufInputStream(byteBuf).use { stream ->
                    packet.data = read(stream, ByteOrder.BIG_ENDIAN, true)
                }
            } catch (e: Exception) {
                SyncEntityPropertyPacket.log.error("", e)
            }

            return packet
        }
    }
}
