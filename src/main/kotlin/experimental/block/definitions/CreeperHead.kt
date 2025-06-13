package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CreeperHead :
    BlockDefinition(
        identifier = "minecraft:creeper_head",
        states = listOf(CommonStates.facingDirection)
    )
