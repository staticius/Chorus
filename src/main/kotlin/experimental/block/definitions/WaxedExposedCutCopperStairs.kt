package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WaxedExposedCutCopperStairs : BlockDefinition(
    identifier = "minecraft:waxed_exposed_cut_copper_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
