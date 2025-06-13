package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleOakSapling :
    BlockDefinition(
        identifier = "minecraft:pale_oak_sapling",
        states = listOf(CommonStates.ageBit)
    )
