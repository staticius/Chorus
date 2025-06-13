package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleOakTrapdoor : BlockDefinition(
    identifier = "minecraft:pale_oak_trapdoor",
    states = listOf(
        CommonStates.direction,
        CommonStates.openBit,
        CommonStates.upsideDownBit
    )
)
