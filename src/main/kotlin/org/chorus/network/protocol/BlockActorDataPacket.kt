package org.chorus.network.protocol

import io.netty.buffer.ByteBufInputStream
import org.chorus.math.BlockVector3
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.connection.util.HandleByteBuf
import java.nio.ByteOrder

class BlockActorDataPacket(
    var blockPosition: BlockVector3,
    var actorDataTags: CompoundTag
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(this.blockPosition)
        byteBuf.writeBytes(NBTIO.write(actorDataTags, ByteOrder.LITTLE_ENDIAN, true))
    }

    override fun pid(): Int {
        return ProtocolInfo.BLOCK_ACTOR_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<BlockActorDataPacket> {
        override fun decode(byteBuf: HandleByteBuf): BlockActorDataPacket {
            return BlockActorDataPacket(
                blockPosition = byteBuf.readBlockVector3(),
                actorDataTags = ByteBufInputStream(byteBuf).use {
                    NBTIO.read(it, ByteOrder.LITTLE_ENDIAN, true)
                }
            )
        }
    }
}
