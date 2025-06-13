package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SmallDripleafBlock : BlockDefinition(
    identifier = "minecraft:small_dripleaf_block",
    states = listOf(
        CommonStates.minecraftCardinalDirection,
        CommonStates.upperBlockBit
    )
)
