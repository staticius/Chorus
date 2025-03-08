package cn.nukkit.network.protocol.types

import cn.nukkit.entity.Entity.getId
import lombok.*

enum class Platform(@field:Getter override val name: String, @field:Getter private val id: Int) {
    UNKNOWN("Unknown", -1),
    ANDROID("Android", 1),
    IOS("iOS", 2),
    MAC_OS("macOS", 3),
    FIRE_OS("FireOS", 4),
    GEAR_VR("Gear VR", 5),
    HOLOLENS("Hololens", 6),
    WINDOWS_10("Windows", 7),
    WINDOWS("Windows", 8),
    DEDICATED("Dedicated", 9),
    TVOS("TVOS", 10),
    PLAYSTATION("PlayStation", 11),
    SWITCH("Switch", 12),
    XBOX_ONE("Xbox One", 13),
    WINDOWS_PHONE("Windows Phone", 14),
    LINUX("Linux", 15);


    companion object {
        private val PLATFORM_BY_ID: MutableMap<Int, Platform> = HashMap()

        init {
            for (platform in entries) {
                PLATFORM_BY_ID[platform.getId()] = platform
            }
        }

        fun getPlatformByID(id: Int): Platform? {
            if (PLATFORM_BY_ID.containsKey(id)) {
                return PLATFORM_BY_ID[id]
            }

            return UNKNOWN
        }

        var VALUES: Array<Platform> = entries.toTypedArray()
    }
}