package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Lilac : BlockDefinition(
    identifier = "minecraft:lilac",
    states = listOf(CommonStates.upperBlockBit)
)
