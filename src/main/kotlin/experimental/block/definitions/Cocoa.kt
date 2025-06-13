package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Cocoa :
    BlockDefinition(
        identifier = "minecraft:cocoa",
        states = listOf(
            CommonStates.age,
            CommonStates.direction
        )
    )
