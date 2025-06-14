package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LitSmoker :
    BlockDefinition(identifier = "minecraft:lit_smoker", states = listOf(CommonStates.minecraftCardinalDirection))
