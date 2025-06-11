package org.chorus_oss.chorus

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import java.util.*

/**
 * Describes an offline player.
 *
 * @see org.chorus_oss.chorus.Player
 */
class OfflinePlayer(override var uuid: UUID) :
    IPlayer {
    private val namedTag: CompoundTag

    init {
        var nbt: CompoundTag?
        nbt = Server.instance.getOfflinePlayerData(uuid, false)
        if (nbt == null) {
            nbt = CompoundTag()
        }
        this.namedTag = nbt

        namedTag.putLong("UUIDMost", uuid.mostSignificantBits)
        namedTag.putLong("UUIDLeast", uuid.leastSignificantBits)
    }

    override val isOnline: Boolean
        get() = this.player != null

    override val name: String?
        get() {
            if (namedTag.contains("NameTag")) {
                return namedTag.getString("NameTag")
            }
            return null
        }

    override var isOp: Boolean
        get() = Server.instance.isOp(name!!.lowercase())
        set(value) {
            if (value) {
                Server.instance.addOp(name!!.lowercase())
            } else {
                Server.instance.removeOp(name!!.lowercase())
            }
        }

    override var isBanned: Boolean
        get() = Server.instance.bannedPlayers.isBanned(this.name)
        set(value) {
            if (value) {
                Server.instance.bannedPlayers.addBan(this.name!!, null, null, null)
            } else {
                Server.instance.bannedPlayers.remove(this.name!!)
            }
        }

    override var isWhitelisted: Boolean
        get() = Server.instance.isWhitelisted(name!!.lowercase())
        set(value) {
            if (value) {
                Server.instance.addWhitelist(name!!.lowercase())
            } else {
                Server.instance.removeWhitelist(name!!.lowercase())
            }
        }

    override val player: Player?
        get() = if (name != null) Server.instance.getPlayerExact(name!!) else null

    override val firstPlayed: Long
        get() = namedTag.getLong("firstPlayed")

    override val lastPlayed: Long
        get() = namedTag.getLong("lastPlayed")

    override fun hasPlayedBefore(): Boolean {
        return true
    }

}
