package org.chorus.network.protocol

import org.chorus.math.BlockVector3
import org.chorus.network.connection.util.HandleByteBuf


class LecternUpdatePacket : DataPacket() {
    var page: Int = 0
    var totalPages: Int = 0
    lateinit var blockPosition: BlockVector3

    override fun pid(): Int {
        return ProtocolInfo.LECTERN_UPDATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<LecternUpdatePacket> {
        override fun decode(byteBuf: HandleByteBuf): LecternUpdatePacket {
            val packet = LecternUpdatePacket()

            packet.page = byteBuf.readUnsignedByte().toInt()
            packet.totalPages = byteBuf.readUnsignedByte().toInt()
            packet.blockPosition = byteBuf.readBlockVector3()

            return packet
        }
    }
}
