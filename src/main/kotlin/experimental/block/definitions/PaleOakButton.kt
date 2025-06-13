package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleOakButton : BlockDefinition(
    identifier = "minecraft:pale_oak_button",
    states = listOf(
        CommonStates.buttonPressedBit,
        CommonStates.facingDirection
    )
)
