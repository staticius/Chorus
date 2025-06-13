package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Kelp : BlockDefinition(
    identifier = "minecraft:kelp",
    states = listOf(CommonStates.kelpAge)
)
