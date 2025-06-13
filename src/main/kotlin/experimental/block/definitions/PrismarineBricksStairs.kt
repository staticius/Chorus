package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PrismarineBricksStairs : BlockDefinition(
    identifier = "minecraft:prismarine_bricks_stairs",
    states = listOf(
        CommonStates.upsideDownBit,
        CommonStates.weirdoDirection
    )
)
