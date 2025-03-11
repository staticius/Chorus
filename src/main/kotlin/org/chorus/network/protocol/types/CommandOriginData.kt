package org.chorus.network.protocol.types


import java.util.*

/**
 * @author SupremeMortal (Nukkit project)
 */

class CommandOriginData(
    val type: Origin, val uuid: UUID, val requestId: String, //event
    private val varlong: Long?
) {
    init {
        this.varlong = varlong
    }

    val varLong: OptionalLong
        get() {
            if (varlong == null) {
                return OptionalLong.empty()
            }
            return OptionalLong.of(varlong)
        }

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
