package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.utils.Identifier

abstract class BlockDefinition(
    val identifier: String,
    val states: List<BlockState<*>> = emptyList(),
    val components: List<BlockComponent> = emptyList(),
    val permutations: List<BlockPermutationDefinition> = emptyList(),
) {
    init {
        Identifier.assertValid(identifier)
        require(states.fold(1) { acc, state -> acc * state.values.size } <= 65536) { "BlockDefinition \"$identifier\" exceeds the permutation limit" }
    }

    val allPermutations: List<BlockPermutation> = BlockPermutation.getPermutations(identifier, states)
    val defaultPermutation: BlockPermutation = allPermutations.first()
}