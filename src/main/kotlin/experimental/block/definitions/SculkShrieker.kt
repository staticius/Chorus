package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SculkShrieker : BlockDefinition(
    identifier = "minecraft:sculk_shrieker",
    states = listOf(CommonStates.active, CommonStates.canSummon)
)
