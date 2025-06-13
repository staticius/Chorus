package org.chorus_oss.chorus.experimental.utils

import org.chorus_oss.chorus.block.BlockID
import org.chorus_oss.chorus.block.BlockStateImpl
import org.chorus_oss.chorus.block.property.type.BlockPropertyType
import org.chorus_oss.chorus.block.property.type.BlockPropertyType.BlockPropertyValue
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.nbt.tag.LinkedCompoundTag
import org.chorus_oss.chorus.nbt.tag.TreeMapCompoundTag
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.utils.HashUtils.fnv1a_32_nbt

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

    fun getHash(identifier: String, state: Map<String, Any>): Int {
        if (identifier == BlockID.UNKNOWN) {
            return -2 // This is special case
        }

        val states = TreeMapCompoundTag()
        for ((identifier, value) in state) {
            when (value) {
                is Int -> states.putInt(
                    identifier, value
                )

                is String -> states.putString(
                    identifier, value
                )

                is Boolean -> states.putByte(
                    identifier, if (value) 1 else 0
                )
            }
        }

        val tag = CompoundTag().putString("name", identifier).putCompound("states", states)
        return fnv1a_32_nbt(tag)
    }

    fun getTag(
        identifier: String, state: Map<String, Any>
    ): CompoundTag {
        val states = TreeMapCompoundTag()
        for ((identifier, value) in state) {
            when (value) {
                is Int -> states.putInt(
                    identifier, value
                )

                is String -> states.putString(
                    identifier, value
                )

                is Boolean -> states.putByte(
                    identifier, if (value) 1 else 0
                )
            }
        }

        return LinkedCompoundTag().apply {
            putString("name", identifier)
            putCompound("states", states)
            putInt("version", ProtocolInfo.BLOCK_STATE_VERSION_NO_REVISION)
        }
    }
}