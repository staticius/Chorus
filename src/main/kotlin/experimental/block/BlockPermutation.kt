package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.block.BlockID
import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.experimental.utils.BlockStates
import org.chorus_oss.chorus.nbt.tag.CompoundTag

data class BlockPermutation(
    val identifier: String,
    val states: Map<String, Any>,
    val components: List<BlockComponent> = emptyList(),
) {
    val hash: Int = BlockStates.getHash(identifier, states)
    val tag: CompoundTag = BlockStates.getTag(identifier, states)

    fun <T : Any> getState(state: BlockState<T>): T? {
        return getState<T>(state.identifier)
    }

    @JvmName("getStateTyped")
    fun <T : Any> getState(identifier: String): T? {
        @Suppress("UNCHECKED_CAST")
        return getState(identifier) as? T?
    }

    fun getState(identifier: String): Any? {
        return this.states[identifier]
    }

    fun <T : Any> withState(blockState: BlockState<T>, value: T): BlockPermutation {
        return withState(blockState.identifier, value)
    }

    fun withState(identifier: String, value: Any): BlockPermutation {
        this.validateState(identifier, value)

        return this.copy(states = this.states + (identifier to value))
    }

    fun withStates(vararg states: Pair<BlockState<*>, Any>): BlockPermutation {
        return withStates(states.toMap())
    }

    fun withStates(states: Map<BlockState<*>, Any>): BlockPermutation {
        return withStates(states.entries.associate { it.key.identifier to it.value })
    }

    @JvmName("withStatesByIdentifier")
    fun withStates(vararg states: Pair<String, Any>): BlockPermutation {
        return withStates(states.toMap())
    }

    @JvmName("withStatesByIdentifier")
    fun withStates(states: Map<String, Any>): BlockPermutation {
        states.forEach { this.validateState(it.key, it.value) }

        return this.copy(states = this.states + states)
    }

    private fun validateState(identifier: String, value: Any) {
        val definition = requireNotNull(BlockRegistry[this.identifier]) {
            "BlockDefinition not found for \"${this.identifier}\""
        }

        val validValues = requireNotNull(definition.stateValues[identifier]) {
            "BlockState \"${identifier}\" is invalid for \"${this.identifier}\""
        }

        require(value in validValues) {
            "Value \"$value\" is invalid for BlockState \"${identifier}\""
        }
    }

    companion object {
        private val UnknownBlockPermutations = mutableMapOf<Int, BlockPermutation>()

        fun getUnknown(hash: Int, tag: CompoundTag): BlockPermutation {
            return UnknownBlockPermutations.computeIfAbsent(
                hash
            ) {
                BlockPermutation(BlockID.UNKNOWN, emptyMap()).apply {
                    tag.putCompound("Block", tag)
                }
            }
        }
    }
}
