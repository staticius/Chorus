package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CommandBlock : BlockDefinition(
    identifier = "minecraft:command_block",
    states = listOf(
        CommonStates.conditionalBit,
        CommonStates.facingDirection
    )
)
