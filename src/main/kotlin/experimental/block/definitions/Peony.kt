package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Peony : BlockDefinition(
    identifier = "minecraft:peony",
    states = listOf(CommonStates.upperBlockBit)
)
