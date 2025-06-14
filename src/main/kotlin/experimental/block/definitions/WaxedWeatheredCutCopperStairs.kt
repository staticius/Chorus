package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WaxedWeatheredCutCopperStairs : BlockDefinition(
    identifier = "minecraft:waxed_weathered_cut_copper_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
