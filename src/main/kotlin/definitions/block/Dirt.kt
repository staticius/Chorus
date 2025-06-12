package org.chorus_oss.chorus.definitions.block

object Dirt : BlockDefinition(
    identifier = "minecraft:dirt",
    permutations = listOf(
        BlockPermutation(
            condition = { it.identifier == "minecraft:dirt" },
            components = emptyList(),
        )
    )
)