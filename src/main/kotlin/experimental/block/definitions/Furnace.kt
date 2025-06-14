package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Furnace :
    BlockDefinition(identifier = "minecraft:furnace", states = listOf(CommonStates.minecraftCardinalDirection))
