package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Vine : BlockDefinition(identifier = "minecraft:vine", states = listOf(CommonStates.vineDirectionBits))
