package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Beehive :
    BlockDefinition(identifier = "minecraft:beehive", states = listOf(CommonStates.direction, CommonStates.honeyLevel))
