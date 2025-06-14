package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.block.BlockID
import org.chorus_oss.chorus.experimental.block.state.BlockState
import org.chorus_oss.chorus.experimental.utils.BlockStates
import org.chorus_oss.chorus.nbt.tag.CompoundTag

data class BlockPermutation(
    val identifier: String,
    val states: Map<String, Any>
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
        val existing = requireNotNull(this.states[identifier]) {
            "BlockDefinition \"${this.identifier}\" does not support state \"${identifier}\""
        }

        require(existing::class == value::class) {
            "BlockState \"${identifier}\" has type ${existing::class.simpleName}, but got type ${value::class.simpleName}"
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

        fun getPermutations(identifier: String, states: List<BlockState<*>>): List<BlockPermutation> {
            return states.fold(listOf(mapOf<String, Any>())) { acc, state ->
                    acc.flatMap { map ->
                        state.values.map {
                            map + (state.identifier to it)
                        }
                    }
                }.map { BlockPermutation(identifier, it) }
        }
    }
}
