package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.EduSharedUriResource


class EduUriResourcePacket : DataPacket() {
    var eduSharedUriResource: EduSharedUriResource? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(eduSharedUriResource!!.buttonName)
        byteBuf.writeString(eduSharedUriResource!!.linkUri)
    }

    override fun pid(): Int {
        return ProtocolInfo.EDU_URI_RESOURCE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<EduUriResourcePacket> {
        override fun decode(byteBuf: HandleByteBuf): EduUriResourcePacket {
            val packet = EduUriResourcePacket()

            val buttonName = byteBuf.readString()
            val linkUri = byteBuf.readString()
            packet.eduSharedUriResource = EduSharedUriResource(buttonName, linkUri)

            return packet
        }
    }
}
