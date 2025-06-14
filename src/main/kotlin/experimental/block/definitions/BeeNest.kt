package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BeeNest :
    BlockDefinition(identifier = "minecraft:bee_nest", states = listOf(CommonStates.direction, CommonStates.honeyLevel))
