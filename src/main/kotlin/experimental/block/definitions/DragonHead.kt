package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DragonHead :
    BlockDefinition(
        identifier = "minecraft:dragon_head",
        states = listOf(CommonStates.facingDirection)
    )
