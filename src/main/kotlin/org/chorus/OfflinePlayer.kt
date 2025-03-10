package org.chorus

import org.chorus.metadata.MetadataValue
import org.chorus.nbt.tag.CompoundTag
import org.chorus.plugin.Plugin
import java.util.*

/**
 * 描述一个不在线的玩家的类。<br></br>
 * Describes an offline player.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @see org.chorus.Player
 *
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
class OfflinePlayer @JvmOverloads constructor(override val server: Server, uuid: UUID?, name: String? = null) :
    IPlayer {
    private val namedTag: CompoundTag

    constructor(server: Server, name: String?) : this(server, null, name)

    /**
     * 初始化这个`OfflinePlayer`对象。<br></br>
     * Initializes the object `OfflinePlayer`.
     *
     * @param server 这个玩家所在服务器的`Server`对象。<br></br>
     * The server this player is in, as a `Server` object.
     * @param uuid   这个玩家的UUID。<br></br>
     * UUID of this player.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    init {
        var nbt: CompoundTag?
        nbt = if (uuid != null) {
            server.getOfflinePlayerData(uuid, false)
        } else if (name != null) {
            server.getOfflinePlayerData(name, false)
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

    override val uniqueId: UUID?
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

    override fun getServer(): Server? {
        return server
    }

    override var isOp: Boolean
        get() = server.isOp(name!!.lowercase())
        set(value) {
            if (value == field) {
                return
            }

            if (value) {
                server.addOp(name!!.lowercase())
            } else {
                server.removeOp(name!!.lowercase())
            }
        }

    override var isBanned: Boolean
        get() = server.nameBans.isBanned(this.name)
        set(value) {
            if (value) {
                server.nameBans.addBan(this.name, null, null, null)
            } else {
                server.nameBans.remove(this.name)
            }
        }

    override var isWhitelisted: Boolean
        get() = server.isWhitelisted(name!!.lowercase())
        set(value) {
            if (value) {
                server.addWhitelist(name!!.lowercase())
            } else {
                server.removeWhitelist(name!!.lowercase())
            }
        }

    override val player: Player
        get() = server.getPlayerExact(name!!)!!

    override val firstPlayed: Long?
        get() = if (this.namedTag != null) namedTag.getLong("firstPlayed") else null

    override val lastPlayed: Long?
        get() = if (this.namedTag != null) namedTag.getLong("lastPlayed") else null

    override fun hasPlayedBefore(): Boolean {
        return this.namedTag != null
    }

    override fun setMetadata(metadataKey: String, newMetadataValue: MetadataValue) {
        server.playerMetadata.setMetadata(this, metadataKey, newMetadataValue)
    }

    override fun getMetadata(metadataKey: String): List<MetadataValue?>? {
        return server.playerMetadata.getMetadata(this, metadataKey)
    }

    override fun getMetadata(metadataKey: String, plugin: Plugin): MetadataValue? {
        return server.playerMetadata.getMetadata(this, metadataKey, plugin)
    }

    override fun hasMetadata(metadataKey: String): Boolean {
        return server.playerMetadata.hasMetadata(this, metadataKey)
    }

    override fun hasMetadata(metadataKey: String, plugin: Plugin): Boolean {
        return server.playerMetadata.hasMetadata(this, metadataKey, plugin)
    }

    override fun removeMetadata(metadataKey: String, owningPlugin: Plugin) {
        server.playerMetadata.removeMetadata(this, metadataKey, owningPlugin)
    }
}
