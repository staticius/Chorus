package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WaxedOxidizedCopperTrapdoor : BlockDefinition(
    identifier = "minecraft:waxed_oxidized_copper_trapdoor",
    states = listOf(
        CommonStates.direction,
        CommonStates.openBit,
        CommonStates.upsideDownBit
    )
)
