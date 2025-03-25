package org.chorus.network.protocol.types

enum class InputMode {
    UNDEFINED,
    MOUSE,
    TOUCH,
    GAME_PAD,
    MOTION_CONTROLLER,
    COUNT;

    companion object {
        private val VALUES = entries.toTypedArray()

        fun fromOrdinal(ordinal: Int): InputMode {
            return VALUES[ordinal]
        }
    }
}
