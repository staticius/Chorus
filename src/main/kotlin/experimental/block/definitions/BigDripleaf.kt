package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BigDripleaf : BlockDefinition(
    identifier = "minecraft:big_dripleaf",
    states = listOf(
        CommonStates.bigDripleafHead,
        CommonStates.bigDripleafTilt,
        CommonStates.minecraftCardinalDirection
    )
)
