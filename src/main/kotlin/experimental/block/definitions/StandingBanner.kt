package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StandingBanner :
    BlockDefinition(identifier = "minecraft:standing_banner", states = listOf(CommonStates.groundSignDirection))
