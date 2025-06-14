package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Bamboo : BlockDefinition(
    identifier = "minecraft:bamboo",
    states = listOf(CommonStates.ageBit, CommonStates.bambooLeafSize, CommonStates.bambooStalkThickness)
)
