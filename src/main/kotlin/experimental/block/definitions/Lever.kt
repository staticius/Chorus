package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Lever :
    BlockDefinition(
        identifier = "minecraft:lever",
        states = listOf(
            CommonStates.leverDirection,
            CommonStates.openBit
        )
    )
