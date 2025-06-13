package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object JungleSapling :
    BlockDefinition(
        identifier = "minecraft:jungle_sapling",
        states = listOf(CommonStates.ageBit)
    )
