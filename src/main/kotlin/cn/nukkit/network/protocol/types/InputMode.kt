package cn.nukkit.network.protocol.types

enum class InputMode(override val ordinal: Int) {
    UNDEFINED(0),
    MOUSE(1),
    TOUCH(2),
    GAME_PAD(3),
    MOTION_CONTROLLER(4),
    COUNT(5);

    companion object {
        private val VALUES = entries.toTypedArray()

        fun fromOrdinal(ordinal: Int): InputMode {
            return VALUES[ordinal]
        }
    }
}
