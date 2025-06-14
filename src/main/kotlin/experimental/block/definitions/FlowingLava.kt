package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object FlowingLava :
    BlockDefinition(identifier = "minecraft:flowing_lava", states = listOf(CommonStates.liquidDepth))
