package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedDarkOakWood :
    BlockDefinition(
        identifier = "minecraft:stripped_dark_oak_wood",
        states = listOf(CommonStates.pillarAxis)
    )
