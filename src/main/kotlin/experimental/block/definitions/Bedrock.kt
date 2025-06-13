package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Bedrock : BlockDefinition(
    identifier = "minecraft:bedrock",
    states = listOf(CommonStates.infiniburnBit)
)
