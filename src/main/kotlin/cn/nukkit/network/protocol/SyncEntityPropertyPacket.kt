package cn.nukkit.network.protocol

import cn.nukkit.nbt.NBTIO.read
import cn.nukkit.nbt.NBTIO.write
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.connection.util.HandleByteBuf
import io.netty.buffer.ByteBufInputStream
import lombok.*
import lombok.extern.slf4j.Slf4j
import java.nio.ByteOrder

@Slf4j
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
