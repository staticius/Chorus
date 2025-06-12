package org.chorus_oss.chorus.definitions.block

import org.chorus_oss.chorus.block.property.type.BlockPropertyType

abstract class BlockDefinition(
    val identifier: String,
    val states: List<BlockPropertyType<*>> = emptyList(),
    val components: List<BlockComponent> = emptyList(),
    val permutations: List<BlockPermutation> = emptyList(),
) {}