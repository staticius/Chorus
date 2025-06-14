package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Ladder : BlockDefinition(identifier = "minecraft:ladder", states = listOf(CommonStates.facingDirection))
