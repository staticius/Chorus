package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.utils.Identifier

abstract class BlockDefinition(
    val identifier: String,
    val states: List<BlockState<*>> = emptyList(),
    val components: List<BlockComponent> = emptyList(),
    val permutations: List<BlockPermutationDefinition> = emptyList(),
) {
    val defaultPermutation: BlockPermutation = BlockPermutation.getDefault(identifier, states)

    init {
        Identifier.assertValid(identifier)
        require(states.sumOf { it.size } <= 16) { "BlockDefinition \"$identifier\" exceeds the state limit" }
    }
}