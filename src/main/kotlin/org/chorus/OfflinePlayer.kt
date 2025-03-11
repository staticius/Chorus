package org.chorus

import org.chorus.metadata.MetadataValue
import org.chorus.nbt.tag.CompoundTag
import org.chorus.plugin.Plugin
import java.util.*

/**
 * Describes an offline player.
 *
 * @see org.chorus.Player
 */
class OfflinePlayer @JvmOverloads constructor(uuid: UUID?, name: String? = null) :
    IPlayer {
    private val namedTag: CompoundTag

    constructor(name: String?) : this(null, name)

    init {
        var nbt: CompoundTag?
        nbt = if (uuid != null) {
            Server.instance.getOfflinePlayerData(uuid, false)
        } else if (name != null) {
            Server.instance.getOfflinePlayerData(name, false)
        } else {
            throw IllegalArgumentException("Name and UUID cannot both be null")
        }
        if (nbt == null) {
            nbt = CompoundTag()
        }
        this.namedTag = nbt

        if (uuid != null) {
            namedTag.putLong("UUIDMost", uuid.mostSignificantBits)
            namedTag.putLong("UUIDLeast", uuid.leastSignificantBits)
        } else {
            namedTag.putString("NameTag", name!!)
        }
    }

    override val isOnline: Boolean
        get() = this.player != null

    override val name: String?
        get() {
            if (namedTag != null && namedTag.contains("NameTag")) {
                return namedTag.getString("NameTag")
            }
            return null
        }

    override val uuid: UUID?
        get() {
            if (namedTag != null) {
                val least = namedTag.getLong("UUIDLeast")
                val most = namedTag.getLong("UUIDMost")

                if (least != 0L && most != 0L) {
                    return UUID(most, least)
                }
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

    override val player: Player
        get() = Server.instance.getPlayerExact(name!!)!!

    override val firstPlayed: Long
        get() = namedTag.getLong("firstPlayed")

    override val lastPlayed: Long
        get() = namedTag.getLong("lastPlayed")

    override fun hasPlayedBefore(): Boolean {
        return true
    }

    override fun setMetadata(metadataKey: String, newMetadataValue: MetadataValue) {
        Server.instance.playerMetadata.setMetadata(this, metadataKey, newMetadataValue)
    }

    override fun getMetadata(metadataKey: String): List<MetadataValue?>? {
        return Server.instance.playerMetadata.getMetadata(this, metadataKey)
    }

    override fun getMetadata(metadataKey: String, plugin: Plugin): MetadataValue? {
        return Server.instance.playerMetadata.getMetadata(this, metadataKey, plugin)
    }

    override fun hasMetadata(metadataKey: String): Boolean {
        return Server.instance.playerMetadata.hasMetadata(this, metadataKey)
    }

    override fun hasMetadata(metadataKey: String, plugin: Plugin): Boolean {
        return Server.instance.playerMetadata.hasMetadata(this, metadataKey, plugin)
    }

    override fun removeMetadata(metadataKey: String, owningPlugin: Plugin) {
        Server.instance.playerMetadata.removeMetadata(this, metadataKey, owningPlugin)
    }
}
