package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.math.BlockVector3
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class OpenSignPacket : DataPacket() {
    @JvmField
    var position: BlockVector3? = null

    @JvmField
    var frontSide: Boolean = false

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(position!!)
        byteBuf.writeBoolean(frontSide)
    }

    override fun pid(): Int {
        return ProtocolInfo.OPEN_SIGN
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<OpenSignPacket> {
        override fun decode(byteBuf: HandleByteBuf): OpenSignPacket {
            val packet = OpenSignPacket()

            packet.position = byteBuf.readBlockVector3()
            packet.frontSide = byteBuf.readBoolean()

            return packet
        }
    }
}
