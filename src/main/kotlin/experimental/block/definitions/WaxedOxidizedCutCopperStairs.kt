package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WaxedOxidizedCutCopperStairs : BlockDefinition(
    identifier = "minecraft:waxed_oxidized_cut_copper_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection)
)
