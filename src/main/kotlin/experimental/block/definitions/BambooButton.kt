package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BambooButton : BlockDefinition(
    identifier = "minecraft:bamboo_button",
    states = listOf(CommonStates.buttonPressedBit, CommonStates.facingDirection)
)
