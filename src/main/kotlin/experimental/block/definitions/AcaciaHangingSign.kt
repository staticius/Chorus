package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AcaciaHangingSign : BlockDefinition(
    identifier = "minecraft:acacia_hanging_sign",
    states = listOf(
        CommonStates.attachedBit,
        CommonStates.facingDirection,
        CommonStates.groundSignDirection,
        CommonStates.hanging
    )
)