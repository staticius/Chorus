package org.chorus.config

import org.chorus.utils.Config
import org.chorus.utils.ConfigSection
import java.io.File

class ServerProperties(dataPath: String) {
    private val properties: Config

    init {
        val file = File(dataPath + "server.properties")
        if (!file.exists()) {
            val defaults = defaultValues
            Config(file.path, Config.PROPERTIES, defaults).save()
        }
        this.properties = Config(
            dataPath + "server.properties", Config.PROPERTIES,
            defaultValues
        )
    }

    private val defaultValues: ConfigSection
        get() {
            val defaults = ConfigSection()
            defaults.put(ServerPropertiesKeys.MOTD.toString(), "PowerNukkitX Server")
            defaults.put(ServerPropertiesKeys.SUB_MOTD.toString(), "powernukkitx.org")
            defaults.put(ServerPropertiesKeys.SERVER_IP.toString(), "0.0.0.0")
            defaults.put(ServerPropertiesKeys.SERVER_PORT.toString(), 19132)
            defaults.put(ServerPropertiesKeys.VIEW_DISTANCE.toString(), 8)
            defaults.put(ServerPropertiesKeys.WHITE_LIST.toString(), false)
            defaults.put(ServerPropertiesKeys.ACHIEVEMENTS.toString(), true)
            defaults.put(ServerPropertiesKeys.ANNOUNCE_PLAYER_ACHIEVEMENTS.toString(), true)
            defaults.put(ServerPropertiesKeys.SPAWN_PROTECTION.toString(), 16)
            defaults.put(ServerPropertiesKeys.MAX_PLAYERS.toString(), 20)
            defaults.put(ServerPropertiesKeys.ALLOW_FLIGHT.toString(), false)
            defaults.put(ServerPropertiesKeys.SPAWN_ANIMALS.toString(), true)
            defaults.put(ServerPropertiesKeys.SPAWN_MOBS.toString(), true)
            defaults.put(ServerPropertiesKeys.GAMEMODE.toString(), 0)
            defaults.put(ServerPropertiesKeys.FORCE_GAMEMODE.toString(), false)
            defaults.put(ServerPropertiesKeys.HARDCORE.toString(), false)
            defaults.put(ServerPropertiesKeys.PVP.toString(), true)
            defaults.put(ServerPropertiesKeys.DIFFICULTY.toString(), 1)
            defaults.put(ServerPropertiesKeys.LEVEL_NAME.toString(), "world")
            defaults.put(ServerPropertiesKeys.LEVEL_SEED.toString(), "")
            defaults.put(ServerPropertiesKeys.ALLOW_NETHER.toString(), true)
            defaults.put(ServerPropertiesKeys.ALLOW_THE_END.toString(), true)
            defaults.put(ServerPropertiesKeys.USE_TERRA.toString(), false)
            defaults.put(ServerPropertiesKeys.ENABLE_QUERY.toString(), false)
            defaults.put(ServerPropertiesKeys.ENABLE_RCON.toString(), false)
            defaults.put(ServerPropertiesKeys.RCON_PASSWORD.toString(), "")
            defaults.put(ServerPropertiesKeys.AUTO_SAVE.toString(), true)
            defaults.put(ServerPropertiesKeys.FORCE_RESOURCES.toString(), false)
            defaults.put(ServerPropertiesKeys.FORCE_RESOURCES_ALLOW_CLIENT_PACKS.toString(), false)
            defaults.put(ServerPropertiesKeys.XBOX_AUTH.toString(), true)
            defaults.put(ServerPropertiesKeys.CHECK_LOGIN_TIME.toString(), false)
            defaults.put(ServerPropertiesKeys.DISABLE_AUTO_BUG_REPORT.toString(), false)
            defaults.put(ServerPropertiesKeys.ALLOW_SHADED.toString(), true)
            defaults.put(ServerPropertiesKeys.SERVER_AUTHORITATIVE_MOVEMENT.toString(), "server-auth")
            defaults.put(ServerPropertiesKeys.NETWORK_ENCRYPTION.toString(), true)
            return defaults
        }

    fun save() {
        properties.save()
    }

    fun reload() {
        properties.reload()
    }

    fun getProperties(): ConfigSection {
        return properties.rootSection
    }

    operator fun get(key: ServerPropertiesKeys, defaultValue: Int): Int {
        val value = properties[key.toString()]
        return if (value is String) {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                // Log the error or handle it as needed
                defaultValue
            }
        } else if (value is Int) {
            value
        } else {
            defaultValue
        }
    }

    fun get(key: ServerPropertiesKeys, defaultValue: String): String {
        val value = properties[key.toString()]
        return if (value is String) {
            value
        } else {
            defaultValue
        }
    }

    operator fun get(key: ServerPropertiesKeys, defaultValue: Boolean): Boolean {
        val value = properties[key.toString()]
        return if (value is String) {
            value.toBoolean()
        } else if (value is Boolean) {
            value
        } else {
            defaultValue
        }
    }

    fun get(key: ServerPropertiesKeys, defaultValue: Long): Long {
        val value = properties[key.toString()]
        return if (value is String) {
            if (!value.isEmpty()) {
                try {
                    value.toLong()
                } catch (e: NumberFormatException) {
                    // Log the error or handle it as needed
                    defaultValue
                }
            } else {
                defaultValue
            }
        } else if (value is Long) {
            value
        } else {
            defaultValue
        }
    }

    operator fun set(key: String?, value: Any?) {
        properties[key] = value
    }

    fun remove(key: String?) {
        properties.remove(key)
    }

    fun exists(key: String?): Boolean {
        return properties.exists(key)
    }
}

