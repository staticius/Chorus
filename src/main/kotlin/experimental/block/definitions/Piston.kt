package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Piston : BlockDefinition(identifier = "minecraft:piston", states = listOf(CommonStates.facingDirection))
