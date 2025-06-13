package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object ZombieHead :
    BlockDefinition(
        identifier = "minecraft:zombie_head",
        states = listOf(CommonStates.facingDirection)
    )
