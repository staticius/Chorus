package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object JungleStandingSign :
    BlockDefinition(
        identifier = "minecraft:jungle_standing_sign",
        states = listOf(CommonStates.groundSignDirection)
    )
