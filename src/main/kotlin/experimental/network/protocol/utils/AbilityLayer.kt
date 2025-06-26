package org.chorus_oss.chorus.experimental.network.protocol.utils

import org.chorus_oss.protocol.types.AbilityLayer
import org.chorus_oss.protocol.types.PlayerAbility
import org.chorus_oss.protocol.types.PlayerAbilitySet

fun AbilityLayer.Companion.from(value: org.chorus_oss.chorus.network.protocol.types.AbilityLayer): AbilityLayer {
    return AbilityLayer(
        AbilityLayer.Companion.Type.entries[value.layerType.ordinal],
        PlayerAbilitySet(value.abilitiesSet.map { PlayerAbility.entries[it.ordinal] }.toMutableSet()),
        PlayerAbilitySet(value.abilityValues.map { PlayerAbility.entries[it.ordinal] }.toMutableSet()),
        value.flySpeed,
        value.verticalFlySpeed,
        value.walkSpeed,
    )
}