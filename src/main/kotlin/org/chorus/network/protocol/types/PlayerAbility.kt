package org.chorus.network.protocol.types

import java.util.*

enum class PlayerAbility(//用于RequestPermissionsPacket的特征位
    val bit: Int = 0
) {
    BUILD(1),
    MINE(2),
    DOORS_AND_SWITCHES(4),
    OPEN_CONTAINERS(8),
    ATTACK_PLAYERS(16),
    ATTACK_MOBS(32),
    OPERATOR_COMMANDS(64),
    TELEPORT(128),

    INVULNERABLE,
    FLYING,
    MAY_FLY,
    INSTABUILD,
    LIGHTNING,
    FLY_SPEED,
    WALK_SPEED,
    MUTED,
    WORLD_BUILDER,
    NO_CLIP,
    PRIVILEGED_BUILDER,
    VERTICAL_FLY_SPEED;

    companion object {
        @JvmField
        val VALUES: List<PlayerAbility> = listOf(*entries.toTypedArray())
    }
}
