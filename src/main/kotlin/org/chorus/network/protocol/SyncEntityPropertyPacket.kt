package org.chorus.network.protocol

import org.chorus.nbt.NBTIO.read
import org.chorus.nbt.NBTIO.write
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.connection.util.HandleByteBuf
import io.netty.buffer.ByteBufInputStream


import java.nio.ByteOrder








class SyncEntityPropertyPacket : DataPacket() {
    var data: CompoundTag? = null

    override fun decode(byteBuf: HandleByteBuf) {
        try {
            ByteBufInputStream(byteBuf).use { stream ->
                this.data = read(stream, ByteOrder.BIG_ENDIAN, true)
            }
        } catch (e: Exception) {
            SyncEntityPropertyPacket.log.error("", e)
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        try {
            byteBuf.writeBytes(write(data!!, ByteOrder.BIG_ENDIAN, true)!!)
        } catch (e: Exception) {
            SyncEntityPropertyPacket.log.error("", e)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SYNC_ENTITY_PROPERTY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
