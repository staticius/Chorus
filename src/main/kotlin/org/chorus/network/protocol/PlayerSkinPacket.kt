package org.chorus.network.protocol

import cn.nukkit.Server
import cn.nukkit.entity.data.Skin
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*
import java.util.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PlayerSkinPacket : DataPacket() {
    @JvmField
    var uuid: UUID? = null
    @JvmField
    var skin: Skin? = null
    @JvmField
    var newSkinName: String? = null
    @JvmField
    var oldSkinName: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        uuid = byteBuf.readUUID()
        skin = byteBuf.readSkin()
        newSkinName = byteBuf.readString()
        oldSkinName = byteBuf.readString()
        if (byteBuf.isReadable) { // -facepalm-
            skin!!.setTrusted(byteBuf.readBoolean())
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUUID(uuid!!)
        byteBuf.writeSkin(skin!!)
        byteBuf.writeString(newSkinName!!)
        byteBuf.writeString(oldSkinName!!)
        byteBuf.writeBoolean(skin!!.isTrusted() || Server.getInstance().settings.playerSettings().forceSkinTrusted())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_SKIN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
