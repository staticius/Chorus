package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LitFurnace :
    BlockDefinition(identifier = "minecraft:lit_furnace", states = listOf(CommonStates.minecraftCardinalDirection))
