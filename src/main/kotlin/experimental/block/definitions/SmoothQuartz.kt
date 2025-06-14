package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SmoothQuartz :
    BlockDefinition(identifier = "minecraft:smooth_quartz", states = listOf(CommonStates.pillarAxis))
