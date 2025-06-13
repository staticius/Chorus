package org.chorus_oss.chorus.experimental.block.state

import kotlin.reflect.KClass

data class BlockState<T : Any>(
    val identifier: String, val values: List<T>
) : Iterable<T> by values {
    init {
        require(values.distinct() == values) {
            "BlockState \"$identifier\" must have no duplicate values"
        }
        require(values.size >= 2) {
            "BlockState \"$identifier\" must have at least 2 values"
        }
        require(values.size <= 16) {
            "BlockState \"$identifier\" must have at most 16 values"
        }
        require(values.all { it is Boolean } || values.all { it is Int } || values.all { it is String }) {
            "BlockState \"$identifier\" must have all Boolean, Int, or String values"
        }
    }

    val defaultValue: T
        get() = values.first()

    val size: Int
        get() = Int.SIZE_BITS - (values.size - 1).countLeadingZeroBits()

    companion object {
        fun from(identifier: String, range: IntRange): BlockState<Int> {
            return BlockState(identifier, range.toList())
        }

        fun from(identifier: String, min: Int = 0, max: Int): BlockState<Int> {
            return from(identifier, min..max)
        }

        fun from(identifier: String, default: Boolean = false): BlockState<Boolean> {
            return BlockState(identifier, listOf(default, !default))
        }

        fun <T : Enum<T>> from(identifier: String, enumClass: Class<T>): BlockState<String> {
            return BlockState(identifier, enumClass.enumConstants.toList().map { it.name.lowercase() })
        }

        fun <T : Enum<T>> from(identifier: String, enumClass: KClass<T>): BlockState<String> {
            return from(identifier, enumClass.java)
        }

        fun <T : Enum<T>> from(identifier: String, enumValues: List<T>): BlockState<String> {
            return BlockState(identifier, enumValues.map { it.name.lowercase() })
        }
    }
}
