package cn.nukkit.network.process

enum class SessionState {
    START,

    LOGIN,

    RESOURCE_PACK,

    ENCRYPTION,

    PRE_SPAWN,

    IN_GAME,

    DEATH
}
