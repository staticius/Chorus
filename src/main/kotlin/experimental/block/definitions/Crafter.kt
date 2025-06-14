package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Crafter : BlockDefinition(
    identifier = "minecraft:crafter",
    states = listOf(CommonStates.crafting, CommonStates.orientation, CommonStates.triggeredBit)
)
