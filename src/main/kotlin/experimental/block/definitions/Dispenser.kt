package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Dispenser : BlockDefinition(
    identifier = "minecraft:dispenser",
    states = listOf(
        CommonStates.facingDirection,
        CommonStates.triggeredBit
    )
)
