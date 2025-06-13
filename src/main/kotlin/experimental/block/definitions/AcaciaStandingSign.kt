package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AcaciaStandingSign : BlockDefinition(
    identifier = "minecraft:acacia_standing_sign",
    states = listOf(
        CommonStates.groundSignDirection
    )
)