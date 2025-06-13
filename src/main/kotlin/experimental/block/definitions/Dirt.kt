package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.BlockPermutationDefinition

object Dirt : BlockDefinition(
    identifier = "minecraft:dirt",
    states = emptyList(),
    components = emptyList(),
    permutations = listOf(
        BlockPermutationDefinition(
            condition = { it.identifier == "minecraft:dirt" },
            components = emptyList(),
        )
    )
)