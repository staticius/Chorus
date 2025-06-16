package org.chorus_oss.chorus.experimental.block

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.utils.Identifier

// TODO: MenuCategory

abstract class BlockDefinition(
    val identifier: String,
    val states: List<BlockState<*>> = emptyList(),
    val components: List<Component<*>> = emptyList(),
    val permutations: List<Permutation> = emptyList(),
) {
    init {
        Identifier.assertValid(identifier)
        require(states.fold(1) { acc, state -> acc * state.values.size } <= 65536) {
            "BlockDefinition \"$identifier\" exceeds the permutation limit"
        }
    }

    data class Permutation(
        val condition: (Map<String, Any>) -> Boolean,
        val components: List<Component<*>>,
    ) {
        constructor(condition: (Map<String, Any>) -> Boolean, vararg components: Component<*>) : this(condition, components.toList())

        constructor(components: List<Component<*>>, condition: (Map<String, Any>) -> Boolean) : this(condition, components)

        constructor(vararg components: Component<*>, condition: (Map<String, Any>) -> Boolean) : this(condition, components.toList())
    }
}