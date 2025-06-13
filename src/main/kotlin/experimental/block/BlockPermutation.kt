package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.block.BlockState

data class BlockPermutation(
    val condition: (BlockState) -> Boolean,
    val components: List<BlockComponent> = emptyList(),
)