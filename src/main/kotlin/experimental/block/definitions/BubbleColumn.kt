package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BubbleColumn :
    BlockDefinition(
        identifier = "minecraft:bubble_column",
        states = listOf(CommonStates.dragDown)
    )
