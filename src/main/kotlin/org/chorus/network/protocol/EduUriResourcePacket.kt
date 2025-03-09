package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.EduSharedUriResource
import lombok.*






class EduUriResourcePacket : DataPacket() {
    var eduSharedUriResource: EduSharedUriResource? = null

    override fun decode(byteBuf: HandleByteBuf) {
        val buttonName = byteBuf.readString()
        val linkUri = byteBuf.readString()
        this.eduSharedUriResource = EduSharedUriResource(buttonName, linkUri)
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(eduSharedUriResource!!.buttonName)
        byteBuf.writeString(eduSharedUriResource!!.linkUri)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.EDU_URI_RESOURCE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
