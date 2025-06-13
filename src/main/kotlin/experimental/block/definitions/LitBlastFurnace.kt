package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LitBlastFurnace : BlockDefinition(
    identifier = "minecraft:lit_blast_furnace",
    states = listOf(CommonStates.minecraftCardinalDirection)
)
