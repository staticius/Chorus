package cn.nukkit.event.server

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.event.HandlerList
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.plugin.Plugin
import cn.nukkit.utils.Binary
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import java.nio.charset.StandardCharsets

/**
 * @author MagicDroidX (Nukkit Project)
 */
class QueryRegenerateEvent @JvmOverloads constructor(server: Server, var timeout: Int = 5) :
    ServerEvent() {
    var serverName: String = server.motd
    private var listPlugins: Boolean
    var plugins: Array<Plugin>
    var playerList: Array<Player>

    private val gameType: String
    var version: String
    private val server_engine: String
    var world: String
    var playerCount: Int
    var maxPlayerCount: Int
    private val whitelist: String
    private val port: Int
    private val ip: String

    var extraData: Map<String, String> = HashMap()

    init {
        this.listPlugins = server.settings.baseSettings().queryPlugins()
        this.plugins =
            if (server.pluginManager == null) Plugin.EMPTY_ARRAY else server.pluginManager.plugins.values.toArray(
                Plugin.EMPTY_ARRAY
            )
        this.playerList = server.onlinePlayers.values.toArray(Player.EMPTY_ARRAY)
        this.gameType = if ((server.gamemode and 0x01) == 0) "SMP" else "CMP"
        this.version = ProtocolInfo.MINECRAFT_VERSION_NETWORK
        this.server_engine = server.name + " " + server.nukkitVersion + " (" + server.gitCommit + ")"
        this.world = if (server.defaultLevel == null) "unknown" else server.defaultLevel.name
        this.playerCount = playerList.size
        this.maxPlayerCount = server.maxPlayers
        this.whitelist = if (server.hasWhitelist()) "on" else "off"
        this.port = server.port
        this.ip = server.ip
    }

    fun canListPlugins(): Boolean {
        return this.listPlugins
    }

    fun setListPlugins(listPlugins: Boolean) {
        this.listPlugins = listPlugins
    }

    val longQuery: ByteBuf
        get() {
            val buf = ByteBufAllocator.DEFAULT.ioBuffer()
            var plist = StringBuilder(this.server_engine)
            if (plugins.size > 0 && this.listPlugins) {
                plist.append(":")
                for (p in this.plugins) {
                    val d = p.description
                    plist.append(" ").append(d.name.replace(";", "").replace(":", "").replace(" ", "_"))
                        .append(" ").append(d.version.replace(";", "").replace(":", "").replace(" ", "_"))
                        .append(";")
                }
                plist = StringBuilder(plist.substring(0, plist.length - 2))
            }

            buf.writeBytes("splitnum".toByteArray())
            buf.writeByte(0x00.toByte().toInt())
            buf.writeByte(128.toByte().toInt())
            buf.writeByte(0x00.toByte().toInt())

            val KVdata =
                LinkedHashMap<String, String>()
            KVdata["hostname"] = serverName
            KVdata["gametype"] = gameType
            KVdata["game_id"] = GAME_ID
            KVdata["version"] = version
            KVdata["server_engine"] = server_engine
            KVdata["plugins"] = plist.toString()
            KVdata["map"] = world
            KVdata["numplayers"] = playerCount.toString()
            KVdata["maxplayers"] = maxPlayerCount.toString()
            KVdata["whitelist"] = whitelist
            KVdata["hostip"] = ip
            KVdata["hostport"] = port.toString()

            for ((key, value) in KVdata) {
                buf.writeBytes(key.toByteArray(StandardCharsets.UTF_8))
                buf.writeByte(0x00.toByte().toInt())
                buf.writeBytes(value.toByteArray(StandardCharsets.UTF_8))
                buf.writeByte(0x00.toByte().toInt())
            }

            buf.writeBytes(byteArrayOf(0x00, 0x01))
            buf.writeBytes("player_".toByteArray())
            buf.writeBytes(byteArrayOf(0x00, 0x00))

            for (player in this.playerList) {
                buf.writeBytes(player.getName().toByteArray(StandardCharsets.UTF_8))
                buf.writeByte(0x00.toByte().toInt())
            }

            buf.writeByte(0x00.toByte().toInt())
            return buf
        }

    val shortQuery: ByteBuf
        get() {
            val buf = ByteBufAllocator.DEFAULT.ioBuffer()
            buf.writeBytes(serverName.toByteArray(StandardCharsets.UTF_8))
            buf.writeByte(0x00.toByte().toInt())
            buf.writeBytes(gameType.toByteArray(StandardCharsets.UTF_8))
            buf.writeByte(0x00.toByte().toInt())
            buf.writeBytes(world.toByteArray(StandardCharsets.UTF_8))
            buf.writeByte(0x00.toByte().toInt())
            buf.writeBytes(
                playerCount.toString()
                    .toByteArray(StandardCharsets.UTF_8)
            )
            buf.writeByte(0x00.toByte().toInt())
            buf.writeBytes(
                maxPlayerCount.toString()
                    .toByteArray(StandardCharsets.UTF_8)
            )
            buf.writeByte(0x00.toByte().toInt())
            buf.writeBytes(Binary.writeLShort(this.port))
            buf.writeBytes(ip.toByteArray(StandardCharsets.UTF_8))
            buf.writeByte(0x00.toByte().toInt())
            return buf
        }

    companion object {
        val handlers: HandlerList = HandlerList()

        private const val GAME_ID = "MINECRAFTPE"
    }
}
