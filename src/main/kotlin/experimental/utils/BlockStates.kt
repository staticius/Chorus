package org.chorus_oss.chorus.experimental.utils

import org.chorus_oss.chorus.block.BlockState
import org.chorus_oss.chorus.block.BlockStateImpl
import org.chorus_oss.chorus.block.property.type.BlockPropertyType
import org.chorus_oss.chorus.block.property.type.BlockPropertyType.BlockPropertyValue

object BlockStates {
    fun getDefaultState(identifier: String, propertyTypeList: List<BlockPropertyType<*>>): BlockStateImpl {
        return BlockStateImpl(identifier, propertyTypeList.map { it.createDefaultValue() })
    }

    fun getAllStates(identifier: String, propertyTypeList: List<BlockPropertyType<*>>): Map<Int, BlockStateImpl> {
        val size = propertyTypeList.size
        if (size == 0) {
            val blockState = BlockStateImpl(identifier, emptyList())
            return mapOf(blockState.hash to blockState)
        }
        val states = mutableMapOf<Int, BlockStateImpl>()

        // to keep track of next element in each of
        // the n arrays
        val indices = MutableList(size) { 0 }

        while (true) {
            // Generate BlockState
            val values = mutableListOf<BlockPropertyValue<*, *, *>>()
            for (i in 0..<size) {
                val type = propertyTypeList[i]
                values.add(type.tryCreateValue(type.validValues[indices[i]])!!)
            }
            val state = BlockStateImpl(identifier, values.toMutableList())
            states[state.hash] = state

            // find the rightmost array that has more
            // elements left after the current element
            // in that array
            var next = size - 1
            while (next >= 0 && (indices[next] + 1 >= propertyTypeList[next].validValues.size)) {
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

        return states
    }

    fun getMaskMap(identifier: String, states: List<BlockPropertyType<*>>): Map<Short, BlockStateImpl> {
        return getAllStates(identifier, states).values.associateBy { it.specialValue() }
    }
}