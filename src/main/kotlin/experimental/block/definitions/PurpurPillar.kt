package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PurpurPillar :
    BlockDefinition(
        identifier = "minecraft:purpur_pillar",
        states = listOf(CommonStates.pillarAxis)
    )
