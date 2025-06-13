package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Lantern : BlockDefinition(
    identifier = "minecraft:lantern",
    states = listOf(CommonStates.hanging)
)
