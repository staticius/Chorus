package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedSpruceWood :
    BlockDefinition(
        identifier = "minecraft:stripped_spruce_wood",
        states = listOf(CommonStates.pillarAxis)
    )
