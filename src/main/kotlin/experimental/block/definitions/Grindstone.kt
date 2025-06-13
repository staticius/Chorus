package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Grindstone : BlockDefinition(
    identifier = "minecraft:grindstone",
    states = listOf(
        CommonStates.attachment,
        CommonStates.direction
    )
)
