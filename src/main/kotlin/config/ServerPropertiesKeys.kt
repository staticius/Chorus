package org.chorus_oss.chorus.config

enum class ServerPropertiesKeys(private val key: String) {
    VIEW_DISTANCE("view-distance"),
    ACHIEVEMENTS("achievements"),
    ANNOUNCE_PLAYER_ACHIEVEMENTS("announce-player-achievements"),
    SPAWN_PROTECTION("spawn-protection"),
    ALLOW_FLIGHT("allow-flight"),
    SPAWN_ANIMALS("spawn-animals"),
    SPAWN_MOBS("spawn-mobs"),
    GAMEMODE("gamemode"),
    FORCE_GAMEMODE("force-gamemode"),
    HARDCORE("hardcore"),
    PVP("pvp"),
    DIFFICULTY("difficulty"),
    LEVEL_NAME("level-name"),
    LEVEL_SEED("level-seed"),
    ALLOW_NETHER("allow-nether"),
    ALLOW_THE_END("allow-the_end"),
    AUTO_SAVE("auto-save"),
    DISABLE_AUTO_BUG_REPORT("disable-auto-bug-report");

    override fun toString(): String {
        return key
    }
}