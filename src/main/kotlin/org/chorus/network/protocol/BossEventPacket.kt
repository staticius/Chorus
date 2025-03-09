package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author CreeperFace
 * @since 30. 10. 2016
 */





class BossEventPacket : DataPacket() {
    @JvmField
    var bossEid: Long = 0
    @JvmField
    var type: Int = 0
    var playerEid: Long = 0
    @JvmField
    var healthPercent: Float = 0f
    @JvmField
    var title: String = ""
    var filteredName: String = ""
    var darkenSky: Short = 0
    @JvmField
    var color: Int = 0
    var overlay: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.bossEid = byteBuf.readEntityUniqueId()
        this.type = byteBuf.readUnsignedVarInt()
        when (this.type) {
            TYPE_REGISTER_PLAYER, TYPE_UNREGISTER_PLAYER, TYPE_QUERY -> this.playerEid = byteBuf.readEntityUniqueId()
            TYPE_SHOW -> {
                this.title = byteBuf.readString()
                this.filteredName = byteBuf.readString()
                this.healthPercent = byteBuf.readFloatLE()
                this.darkenSky = byteBuf.readShort()
                this.color = byteBuf.readUnsignedVarInt()
                this.overlay = byteBuf.readUnsignedVarInt()
            }

            TYPE_UPDATE_PROPERTIES -> {
                this.darkenSky = byteBuf.readShort()
                this.color = byteBuf.readUnsignedVarInt()
                this.overlay = byteBuf.readUnsignedVarInt()
            }

            TYPE_TEXTURE -> {
                this.color = byteBuf.readUnsignedVarInt()
                this.overlay = byteBuf.readUnsignedVarInt()
            }

            TYPE_HEALTH_PERCENT -> this.healthPercent = byteBuf.readFloatLE()
            TYPE_TITLE -> this.title = byteBuf.readString()
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.bossEid)
        byteBuf.writeUnsignedVarInt(this.type)
        when (this.type) {
            TYPE_REGISTER_PLAYER, TYPE_UNREGISTER_PLAYER, TYPE_QUERY -> byteBuf.writeEntityUniqueId(
                this.playerEid
            )

            TYPE_SHOW -> {
                byteBuf.writeString(this.title)
                byteBuf.writeString(this.filteredName)
                byteBuf.writeFloatLE(this.healthPercent)
                byteBuf.writeShort(darkenSky.toInt())
                byteBuf.writeUnsignedVarInt(this.color)
                byteBuf.writeUnsignedVarInt(this.overlay)
            }

            TYPE_UNKNOWN_6 -> {
                byteBuf.writeShort(darkenSky.toInt())
                byteBuf.writeUnsignedVarInt(this.color)
                byteBuf.writeUnsignedVarInt(this.overlay)
            }

            TYPE_TEXTURE -> {
                byteBuf.writeUnsignedVarInt(this.color)
                byteBuf.writeUnsignedVarInt(this.overlay)
            }

            TYPE_HEALTH_PERCENT -> byteBuf.writeFloatLE(this.healthPercent)
            TYPE_TITLE -> {
                byteBuf.writeString(this.title)
                byteBuf.writeString(this.filteredName)
            }
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.BOSS_EVENT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        /* S2C: Shows the bossbar to the player. */
        const val TYPE_SHOW: Int = 0

        /* C2S: Registers a player to a boss fight. */
        const val TYPE_REGISTER_PLAYER: Int = 1
        const val TYPE_UPDATE: Int = 1

        /* S2C: Removes the bossbar from the client. */
        const val TYPE_HIDE: Int = 2

        /* C2S: Unregisters a player from a boss fight. */
        const val TYPE_UNREGISTER_PLAYER: Int = 3

        /* S2C: Sets the bar percentage. */
        const val TYPE_HEALTH_PERCENT: Int = 4

        /* S2C: Sets title of the bar. */
        const val TYPE_TITLE: Int = 5

        /* S2C: Update a player's bossbar properties. */
        const val TYPE_UPDATE_PROPERTIES: Int = 6
        const val TYPE_UNKNOWN_6: Int = TYPE_UPDATE_PROPERTIES

        /* S2C: Sets color and overlay of the bar. */
        const val TYPE_TEXTURE: Int = 7

        /* S2C: Get a player's bossbar information. TODO: 2022/2/9 implement query packet. */
        const val TYPE_QUERY: Int = 8
    }
}
