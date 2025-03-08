package cn.nukkit.network.protocol.types

import io.netty.util.internal.logging.InternalLogger
import io.netty.util.internal.logging.InternalLoggerFactory

enum class TeleportationCause {
    UNKNOWN,
    PROJECTILE,
    CHORUS_FRUIT,
    COMMAND,
    BEHAVIOR;

    companion object {
        private val log: InternalLogger = InternalLoggerFactory.getInstance(TeleportationCause::class.java)

        private val VALUES = entries.toTypedArray()

        fun byId(id: Int): TeleportationCause {
            if (id >= 0 && id < VALUES.size) {
                return VALUES[id]
            }
            log.debug("Unknown teleportation cause ID: {}", id)
            return UNKNOWN
        }
    }
}
