package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WallBanner :
    BlockDefinition(
        identifier = "minecraft:wall_banner",
        states = listOf(CommonStates.facingDirection)
    )
