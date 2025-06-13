package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object FlowingWater :
    BlockDefinition(
        identifier = "minecraft:flowing_water",
        states = listOf(CommonStates.liquidDepth)
    )
