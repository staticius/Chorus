package org.chorus.network.protocol

import org.chorus.Server
import org.chorus.entity.data.Skin
import org.chorus.network.connection.util.HandleByteBuf

import java.util.*


class PlayerListPacket : DataPacket() {
    @JvmField
    var type: Byte = 0

    @JvmField
    var entries: Array<Entry?> = Entry.EMPTY_ARRAY

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(type.toInt())
        byteBuf.writeUnsignedVarInt(entries.size)

        if (this.type == TYPE_ADD) {
            for (entry in this.entries) {
                byteBuf.writeUUID(entry.uuid)

                byteBuf.writeVarLong(entry.entityId)
                byteBuf.writeString(entry.name)
                byteBuf.writeString(entry.xboxUserId)
                byteBuf.writeString(entry.platformChatId)
                byteBuf.writeIntLE(entry.buildPlatform)
                byteBuf.writeSkin(entry.skin!!)
                byteBuf.writeBoolean(entry.isTeacher)
                byteBuf.writeBoolean(entry.isHost)
                byteBuf.writeBoolean(entry.subClient)
            }

            for (entry in this.entries) {
                byteBuf.writeBoolean(
                    entry.trustedSkin || Server.instance.settings.playerSettings().forceSkinTrusted()
                )
            }
        } else {
            for (entry in this.entries) {
                byteBuf.writeUUID(entry.uuid)
            }
        }
    }


    class Entry {
        val uuid: UUID

        var entityId: Long = 0
        var name: String = ""
        var xboxUserId: String = "" //TODO
        var platformChatId: String = "" //TODO
        var buildPlatform: Int = -1
        var skin: Skin? = null
        var isTeacher: Boolean = false
        var isHost: Boolean = false
        var subClient: Boolean = false
        var trustedSkin: Boolean = false

        constructor(uuid: UUID) {
            this.uuid = uuid
        }

        @JvmOverloads
        constructor(uuid: UUID, entityId: Long, name: String, skin: Skin, xboxUserId: String? = "") {
            this.uuid = uuid
            this.entityId = entityId
            this.name = name
            this.skin = skin
            this.trustedSkin = skin.isTrusted()
            this.xboxUserId = xboxUserId ?: ""
        }

        companion object {
            val EMPTY_ARRAY: Array<Entry?> = arrayOfNulls(0)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_LIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val TYPE_ADD: Byte = 0
        const val TYPE_REMOVE: Byte = 1
    }
}
