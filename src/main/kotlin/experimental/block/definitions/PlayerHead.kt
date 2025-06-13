package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PlayerHead :
    BlockDefinition(
        identifier = "minecraft:player_head",
        states = listOf(CommonStates.facingDirection)
    )
