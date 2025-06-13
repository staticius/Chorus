package org.chorus_oss.chorus.experimental.block

import org.chorus_oss.chorus.block.BlockState
import org.chorus_oss.chorus.block.BlockStateImpl
import org.chorus_oss.chorus.block.property.type.BlockPropertyType
import org.chorus_oss.chorus.experimental.utils.BlockStates
import org.chorus_oss.chorus.utils.Identifier

abstract class BlockDefinition(
    val identifier: String,
    val states: List<BlockPropertyType<*>> = emptyList(),
    val components: List<BlockComponent> = emptyList(),
    val permutations: List<BlockPermutation> = emptyList(),
) {
    val maskSize: Int = states.sumOf { it.bitSize.toInt() }.also {
        if (it > 16) throw IllegalStateException("State limit exceeded for block: $identifier")
    }

    val maskMap: Map<Short, BlockStateImpl> = BlockStates.getMaskMap(identifier, states)

    val defaultState: BlockStateImpl = BlockStates.getDefaultState(identifier, states)

    init {
        Identifier.assertValid(identifier)
    }

    fun getState(mask: Short): BlockStateImpl? {
        return maskMap[mask]
    }

//    fun <T> getState(
//        property: BlockPropertyType<T>, value: T
//    ): BlockState {
//        return defaultState.setPropertyValue(this, property, value)
//    }
//
//    fun getState(propertyValue: BlockPropertyValue<*, *, *>): BlockState {
//        return defaultState.setPropertyValue(this, propertyValue)
//    }
//
//    fun getState(vararg values: BlockPropertyValue<*, *, *>): BlockState {
//        return defaultState.setPropertyValues(this, *values)
//    }

    fun hasState(state: BlockState): Boolean {
        return maskMap.containsValue(state)
    }

    fun hasState(mask: Short): Boolean {
        return maskMap.containsKey(mask)
    }

    fun hasProperty(property: BlockPropertyType<*>): Boolean {
        return states.contains(property)
    }
}