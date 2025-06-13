package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object JungleWood :
    BlockDefinition(
        identifier = "minecraft:jungle_wood",
        states = listOf(CommonStates.pillarAxis)
    )
