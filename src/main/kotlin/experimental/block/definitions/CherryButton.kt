package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CherryButton : BlockDefinition(
    identifier = "minecraft:cherry_button",
    states = listOf(CommonStates.buttonPressedBit, CommonStates.facingDirection)
)
