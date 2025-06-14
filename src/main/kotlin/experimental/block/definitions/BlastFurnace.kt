package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BlastFurnace :
    BlockDefinition(identifier = "minecraft:blast_furnace", states = listOf(CommonStates.minecraftCardinalDirection))
