package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MelonStem : BlockDefinition(
    identifier = "minecraft:melon_stem",
    states = listOf(
        CommonStates.facingDirection,
        CommonStates.growth
    )
)
