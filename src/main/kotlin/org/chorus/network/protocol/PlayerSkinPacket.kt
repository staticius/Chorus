package org.chorus.network.protocol

import org.chorus.Server
import org.chorus.entity.data.Skin
import org.chorus.network.connection.util.HandleByteBuf

import java.util.*


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
        byteBuf.writeBoolean(skin!!.isTrusted() || Server.instance.settings.playerSettings().forceSkinTrusted())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_SKIN_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
