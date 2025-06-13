package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.block.BlockState

data class BlockPermutationDefinition(
    val condition: (BlockState) -> Boolean,
    val components: List<BlockComponent> = emptyList(),
)