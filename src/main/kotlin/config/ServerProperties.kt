package org.chorus_oss.chorus.config

import org.chorus_oss.chorus.utils.Config
import org.chorus_oss.chorus.utils.ConfigSection
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
            defaults[ServerPropertiesKeys.VIEW_DISTANCE.toString()] = 8
            defaults[ServerPropertiesKeys.ACHIEVEMENTS.toString()] = true
            defaults[ServerPropertiesKeys.ANNOUNCE_PLAYER_ACHIEVEMENTS.toString()] = true
            defaults[ServerPropertiesKeys.SPAWN_PROTECTION.toString()] = 16
            defaults[ServerPropertiesKeys.ALLOW_FLIGHT.toString()] = false
            defaults[ServerPropertiesKeys.SPAWN_ANIMALS.toString()] = true
            defaults[ServerPropertiesKeys.SPAWN_MOBS.toString()] = true
            defaults[ServerPropertiesKeys.GAMEMODE.toString()] = 0
            defaults[ServerPropertiesKeys.FORCE_GAMEMODE.toString()] = false
            defaults[ServerPropertiesKeys.HARDCORE.toString()] = false
            defaults[ServerPropertiesKeys.PVP.toString()] = true
            defaults[ServerPropertiesKeys.DIFFICULTY.toString()] = 1
            defaults[ServerPropertiesKeys.LEVEL_NAME.toString()] = "world"
            defaults[ServerPropertiesKeys.LEVEL_SEED.toString()] = ""
            defaults[ServerPropertiesKeys.ALLOW_NETHER.toString()] = true
            defaults[ServerPropertiesKeys.ALLOW_THE_END.toString()] = true
            defaults[ServerPropertiesKeys.AUTO_SAVE.toString()] = true
            defaults[ServerPropertiesKeys.DISABLE_AUTO_BUG_REPORT.toString()] = false
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
        return when (val value = properties[key.toString()]) {
            is String -> {
                try {
                    value.toInt()
                } catch (e: NumberFormatException) {
                    defaultValue
                }
            }

            is Int -> value
            else -> defaultValue
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
        return when (val value = properties[key.toString()]) {
            is String -> value.toBoolean()
            is Boolean -> value
            else -> defaultValue
        }
    }

    fun get(key: ServerPropertiesKeys, defaultValue: Long): Long {
        return when (val value = properties[key.toString()]) {
            is String -> {
                try {
                    value.toLong()
                } catch (e: NumberFormatException) {
                    defaultValue
                }
            }

            is Long -> value
            else -> defaultValue
        }
    }

    operator fun set(key: String, value: Any?) {
        properties[key] = value
    }

    fun remove(key: String) {
        properties.remove(key)
    }

    fun exists(key: String): Boolean {
        return properties.exists(key)
    }
}

