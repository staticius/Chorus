package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CrimsonStandingSign :
    BlockDefinition(
        identifier = "minecraft:crimson_standing_sign",
        states = listOf(CommonStates.groundSignDirection)
    )
