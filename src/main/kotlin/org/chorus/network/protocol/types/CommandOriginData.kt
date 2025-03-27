package org.chorus.network.protocol.types

import java.util.*

data class CommandOriginData(
    val type: Origin,
    val uuid: UUID,
    val requestId: String, // event
    val playerId: Long?
) {
    enum class Origin {
        PLAYER,
        BLOCK,
        MINECART_BLOCK,
        DEV_CONSOLE,
        TEST,
        AUTOMATION_PLAYER,
        CLIENT_AUTOMATION,
        DEDICATED_SERVER,
        ENTITY,
        VIRTUAL,
        GAME_ARGUMENT,
        ENTITY_SERVER,
        PRECOMPILED,
        GAME_DIRECTOR_ENTITY_SERVER,
        SCRIPT,
        EXECUTE_CONTEXT
    }
}
