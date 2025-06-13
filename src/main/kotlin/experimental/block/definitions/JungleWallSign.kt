package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object JungleWallSign :
    BlockDefinition(
        identifier = "minecraft:jungle_wall_sign",
        states = listOf(CommonStates.facingDirection)
    )
