package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedAcaciaWood :
    BlockDefinition(
        identifier = "minecraft:stripped_acacia_wood",
        states = listOf(CommonStates.pillarAxis)
    )
