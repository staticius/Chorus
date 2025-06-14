package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.utils.Identifier
import org.chorus_oss.chorus.utils.Loggable

abstract class BlockDefinition(
    val identifier: String,
    val states: List<BlockState<*>> = emptyList(),
    components: List<BlockComponent> = emptyList(),
    permutations: List<Pair<(Map<String, Any>) -> Boolean, List<BlockComponent>>> = emptyList(),
) {
    init {
        Identifier.assertValid(identifier)
        require(states.fold(1) { acc, state -> acc * state.values.size } <= 65536) {
            "BlockDefinition \"$identifier\" exceeds the permutation limit"
        }
    }

    val permutations = states.fold(listOf(emptyMap<String, Any>())) { acc, state ->
        acc.flatMap { map ->
            state.values.map {
                map + (state.identifier to it)
            }
        }
    }.map {
        BlockPermutation(
            identifier,
            it,
            components + permutations.filter { p -> p.first(it) }.flatMap { p -> p.second })
    }

    val defaultPermutation: BlockPermutation = this.permutations.first()

    val stateValues: Map<String, List<Any>> = this.states.associate { it.identifier to it.values }
}