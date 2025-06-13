package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Dropper : BlockDefinition(
    identifier = "minecraft:dropper",
    states = listOf(
        CommonStates.facingDirection,
        CommonStates.triggeredBit
    )
)
