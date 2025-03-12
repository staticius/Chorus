package org.chorus.network.protocol.types

import org.chorus.entity.Entity.getId


@RequiredArgsConstructor
enum class MovementEffectType {
    INVALID(-1),
    GLIDE_BOOST(0);

    private val id = 0

    companion object {
        private val VALUES = entries.toTypedArray()

        fun byId(id: Int): MovementEffectType? {
            for (type in VALUES) {
                if (type.getId() == id) {
                    return type
                }
            }
            return null
        }
    }
}
