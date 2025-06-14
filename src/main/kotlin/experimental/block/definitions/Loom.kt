package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Loom : BlockDefinition(identifier = "minecraft:loom", states = listOf(CommonStates.direction))
