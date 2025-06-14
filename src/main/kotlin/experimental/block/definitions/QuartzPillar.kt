package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object QuartzPillar :
    BlockDefinition(identifier = "minecraft:quartz_pillar", states = listOf(CommonStates.pillarAxis))
