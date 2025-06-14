package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangroveLeaves : BlockDefinition(
    identifier = "minecraft:mangrove_leaves",
    states = listOf(CommonStates.persistentBit, CommonStates.updateBit)
)
