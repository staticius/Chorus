package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RepeatingCommandBlock : BlockDefinition(
    identifier = "minecraft:repeating_command_block",
    states = listOf(CommonStates.conditionalBit, CommonStates.facingDirection)
)
