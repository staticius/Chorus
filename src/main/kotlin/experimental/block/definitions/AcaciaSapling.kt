package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AcaciaSapling :
    BlockDefinition(
        identifier = "minecraft:acacia_sapling",
        states = listOf(CommonStates.ageBit)
    )
