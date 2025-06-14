package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PiglinHead :
    BlockDefinition(identifier = "minecraft:piglin_head", states = listOf(CommonStates.facingDirection))
