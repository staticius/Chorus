package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LitPumpkin :
    BlockDefinition(identifier = "minecraft:lit_pumpkin", states = listOf(CommonStates.minecraftCardinalDirection))
