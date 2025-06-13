package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object InfestedDeepslate :
    BlockDefinition(
        identifier = "minecraft:infested_deepslate",
        states = listOf(CommonStates.pillarAxis)
    )
