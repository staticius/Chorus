package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.chorus.network.protocol.types.SerializedAbilitiesData
import org.chorus_oss.protocol.types.AbilitiesData
import org.chorus_oss.protocol.types.AbilityLayer
import org.chorus_oss.protocol.types.CommandPermission
import org.chorus_oss.protocol.types.PlayerPermission

fun AbilitiesData.Companion.from(value: SerializedAbilitiesData): AbilitiesData {
    return AbilitiesData(
        value.targetPlayerRawID,
        PlayerPermission.entries[value.playerPermissions.ordinal],
        CommandPermission.entries[value.commandPermissions.ordinal],
        value.layers.map(AbilityLayer::from)
    )
}