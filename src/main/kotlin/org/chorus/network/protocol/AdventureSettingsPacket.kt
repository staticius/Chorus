package org.chorus.network.protocol

import org.chorus.Player
import org.chorus.network.connection.util.HandleByteBuf


class AdventureSettingsPacket : DataPacket() {
    var flags: Long = 0
    var commandPermission: Long = PERMISSION_NORMAL.toLong()
    var flags2: Long = 0
    var playerPermission: Long = Player.PERMISSION_MEMBER.toLong()
    var customFlags: Long = 0 //...
    var entityUniqueId: Long = 0 //This is a little-endian long, NOT a var-long. (WTF Mojang)

    override fun decode(byteBuf: HandleByteBuf) {
        this.flags = byteBuf.readUnsignedVarInt().toLong()
        this.commandPermission = byteBuf.readUnsignedVarInt().toLong()
        this.flags2 = byteBuf.readUnsignedVarInt().toLong()
        this.playerPermission = byteBuf.readUnsignedVarInt().toLong()
        this.customFlags = byteBuf.readUnsignedVarInt().toLong()
        this.entityUniqueId = byteBuf.readLongLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(flags.toInt())
        byteBuf.writeUnsignedVarInt(commandPermission.toInt())
        byteBuf.writeUnsignedVarInt(flags2.toInt())
        byteBuf.writeUnsignedVarInt(playerPermission.toInt())
        byteBuf.writeUnsignedVarInt(customFlags.toInt())
        byteBuf.writeLongLE(entityUniqueId)
    }

    fun getFlag(flag: Int): Boolean {
        if ((flag and BITFLAG_SECOND_SET) != 0) {
            return (this.flags2 and flag.toLong()) != 0L
        }
        return (this.flags and flag.toLong()) != 0L
    }

    fun setFlag(flag: Int, value: Boolean) {
        val flags = (flag and BITFLAG_SECOND_SET) != 0

        if (value) {
            if (flags) {
                this.flags2 = this.flags2 or flag.toLong()
            } else {
                this.flags = this.flags or flag.toLong()
            }
        } else {
            if (flags) {
                this.flags2 = this.flags2 and flag.toLong().inv()
            } else {
                this.flags = this.flags and flag.toLong().inv()
            }
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADVENTURE_SETTINGS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val PERMISSION_NORMAL: Int = 0
        const val PERMISSION_OPERATOR: Int = 1
        const val PERMISSION_HOST: Int = 2
        const val PERMISSION_AUTOMATION: Int = 3
        const val PERMISSION_ADMIN: Int = 4

        /**
         * This constant is used to identify flags that should be set on the second field. In a sensible world, these
         * flags would all be set on the same packet field, but as of MCPE 1.2, the new abilities flags have for some
         * reason been assigned a separate field.
         */
        const val BITFLAG_SECOND_SET: Int = 1 shl 16

        const val WORLD_IMMUTABLE: Int = 0x01
        const val NO_PVM: Int = 0x02
        const val NO_MVP: Int = 0x04
        const val SHOW_NAME_TAGS: Int = 0x10
        const val AUTO_JUMP: Int = 0x20
        const val ALLOW_FLIGHT: Int = 0x40
        const val NO_CLIP: Int = 0x80
        const val WORLD_BUILDER: Int = 0x100
        const val FLYING: Int = 0x200
        const val MUTED: Int = 0x400

        const val MINE: Int = 0x01 or BITFLAG_SECOND_SET
        const val DOORS_AND_SWITCHES: Int = 0x02 or BITFLAG_SECOND_SET
        const val OPEN_CONTAINERS: Int = 0x04 or BITFLAG_SECOND_SET
        const val ATTACK_PLAYERS: Int = 0x08 or BITFLAG_SECOND_SET
        const val ATTACK_MOBS: Int = 0x10 or BITFLAG_SECOND_SET
        const val OPERATOR: Int = 0x20 or BITFLAG_SECOND_SET
        const val TELEPORT: Int = 0x80 or BITFLAG_SECOND_SET
        const val BUILD: Int = 0x100 or BITFLAG_SECOND_SET
        const val DEFAULT_LEVEL_PERMISSIONS: Int = 0x200 or BITFLAG_SECOND_SET
    }
}
