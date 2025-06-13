package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.experimental.utils.BlockStates
import org.chorus_oss.chorus.nbt.tag.CompoundTag

data class BlockPermutation(
    val identifier: String,
    val state: Map<String, Any>
) {
    val hash: Int = BlockStates.getHash(identifier, state)
    val tag: CompoundTag = BlockStates.getTag(identifier, state)

    fun <T : Any> getState(state: BlockState<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return this.state[state.identifier] as? T?
    }

    fun <T> getState(identifier: String): T? {
        @Suppress("UNCHECKED_CAST")
        return this.state[identifier] as? T?
    }

    fun getState(identifier: String): Any? {
        return this.state[identifier]
    }

    fun <T : Any> withState(blockState: BlockState<T>, value: T): BlockPermutation {
        return withState(blockState.identifier, value)
    }

    fun withState(identifier: String, value: Any): BlockPermutation {
        this.validateState(identifier, value)

        return this.copy(state = this.state + (identifier to value))
    }

    fun withStates(vararg states: Pair<BlockState<*>, Any>): BlockPermutation {
        return withStates(states.toMap())
    }

    fun withStates(states: Map<BlockState<*>, Any>): BlockPermutation {
        return withStates(states.entries.associate { it.key.identifier to it.value })
    }

    fun withStates(vararg states: Pair<String, Any>): BlockPermutation {
        return withStates(states.toMap())
    }

    fun withStates(states: Map<String, Any>): BlockPermutation {
        states.forEach { this.validateState(it.key, it.value) }

        return this.copy(state = this.state + states)
    }

    private fun validateState(identifier: String, value: Any) {
        val existing = requireNotNull(this.state[identifier]) {
            "BlockDefinition \"${this.identifier}\" does not support state \"${identifier}\""
        }

        require (existing::class == value::class) {
            "BlockState \"${identifier}\" has type ${existing::class.simpleName}, but got type ${value::class.simpleName}"
        }
    }

    companion object {
        fun getDefault(identifier: String, states: List<BlockState<*>>): BlockPermutation {
            return BlockPermutation(
                identifier,
                states.associate { it.identifier to it.defaultValue },
            )
        }
    }
}
