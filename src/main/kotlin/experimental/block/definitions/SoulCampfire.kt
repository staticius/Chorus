package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SoulCampfire : BlockDefinition(
    identifier = "minecraft:soul_campfire",
    states = listOf(
        CommonStates.extinguished,
        CommonStates.minecraftCardinalDirection
    )
)
