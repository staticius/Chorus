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
        @Suppress("UNCHECKED_CAST")
        return this.states[state.identifier] as? T?
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

//    fun withStates(vararg states: Pair<BlockState<*>, Any>): BlockPermutation {
//        return withStates(states.toMap())
//    }

//    fun withStates(states: Map<BlockState<*>, Any>): BlockPermutation {
//        return withStates(states.entries.associate { it.key.identifier to it.value })
//    }

    fun withStates(vararg states: Pair<String, Any>): BlockPermutation {
        return withStates(states.toMap())
    }

    fun withStates(states: Map<String, Any>): BlockPermutation {
        states.forEach { this.validateState(it.key, it.value) }

        return this.copy(states = this.states + states)
    }

    private fun validateState(identifier: String, value: Any) {
        val existing = requireNotNull(this.states[identifier]) {
            "BlockDefinition \"${this.identifier}\" does not support state \"${identifier}\""
        }

        require (existing::class == value::class) {
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
            val size = states.size
            if (size == 0) {
                val blockState = BlockPermutation(identifier, emptyMap())
                return mutableListOf(blockState)
            }
            val permutations = mutableListOf<BlockPermutation>()

            // to keep track of next element in each of
            // the n arrays
            val indices = MutableList(size) { 0 }

            while (true) {
                // Generate BlockState
                val values = mutableMapOf<String, Any>()
                for (i in 0..<size) {
                    val type = states[i]
                    values.put(type.identifier, type.values[indices[i]])
                }
                val state = BlockPermutation(identifier, values)
                permutations.add(state)

                // find the rightmost array that has more
                // elements left after the current element
                // in that array
                var next = size - 1
                while (next >= 0 && (indices[next] + 1 >= states[next].values.size)) {
                    next--
                }

                // no such array is found so no more
                // combinations left
                if (next < 0) break

                // if found move to next element in that
                // array
                indices[next]++

                // for all arrays to the right of this
                // array current index again points to
                // first element
                for (i in next + 1..<size) {
                    indices[i] = 0
                }
            }

            return permutations
        }
    }
}
