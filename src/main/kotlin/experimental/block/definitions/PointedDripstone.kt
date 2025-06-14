package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PointedDripstone : BlockDefinition(
    identifier = "minecraft:pointed_dripstone",
    states = listOf(CommonStates.dripstoneThickness, CommonStates.hanging)
)
