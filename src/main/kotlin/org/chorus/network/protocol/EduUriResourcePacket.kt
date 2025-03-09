package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.EduSharedUriResource
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
