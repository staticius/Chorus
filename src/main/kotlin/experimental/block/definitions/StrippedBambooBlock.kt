package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedBambooBlock :
    BlockDefinition(
        identifier = "minecraft:stripped_bamboo_block",
        states = listOf(CommonStates.pillarAxis)
    )
