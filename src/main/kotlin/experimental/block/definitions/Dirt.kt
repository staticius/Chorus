package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.BlockPermutation

object Dirt : BlockDefinition(
    identifier = "minecraft:dirt",
    permutations = listOf(
        BlockPermutation(
            condition = { it.identifier == "minecraft:dirt" },
            components = emptyList(),
        )
    )
)