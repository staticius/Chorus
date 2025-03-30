package org.chorus.network.protocol

import org.chorus.math.BlockVector3
import org.chorus.network.connection.util.HandleByteBuf

data class BlockPickRequestPacket(
    val position: BlockVector3,
    var withData: Boolean,
    var maxSlots: Int,
) : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.BLOCK_PICK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<BlockPickRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): BlockPickRequestPacket {
            return BlockPickRequestPacket(
                position = byteBuf.readSignedBlockPosition(),
                withData = byteBuf.readBoolean(),
                maxSlots = byteBuf.readByte().toInt()
            )
        }
    }
}
