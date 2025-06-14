package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DecoratedPot :
    BlockDefinition(identifier = "minecraft:decorated_pot", states = listOf(CommonStates.direction))
