package org.chorus.network.protocol

import cn.nukkit.nbt.NBTIO.read
import cn.nukkit.nbt.NBTIO.write
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.connection.util.HandleByteBuf
import io.netty.buffer.ByteBufInputStream
import lombok.*
import java.io.IOException
import java.nio.ByteOrder

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Getter
@Setter
@ToString(exclude = ["namedTag"])
@NoArgsConstructor
@AllArgsConstructor
class BlockEntityDataPacket : DataPacket() {
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var namedTag: CompoundTag? = null

    override fun decode(byteBuf: HandleByteBuf) {
        val v = byteBuf.readBlockVector3()
        this.x = v.x
        this.y = v.y
        this.z = v.z
        try {
            ByteBufInputStream(byteBuf).use { `is` ->
                this.namedTag = read(`is`, ByteOrder.LITTLE_ENDIAN, true)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(this.x, this.y, this.z)
        try {
            byteBuf.writeBytes(write(namedTag!!, ByteOrder.LITTLE_ENDIAN, true)!!)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.BLOCK_ENTITY_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
