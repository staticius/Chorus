package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SweetBerryBush :
    BlockDefinition(
        identifier = "minecraft:sweet_berry_bush",
        states = listOf(CommonStates.growth)
    )
