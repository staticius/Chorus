/*
 Copyright (C) 2020  powernukkit.org - José Roberto de Araújo Júnior
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>
 */
package org.chorus.utils.version

import org.chorus.nbt.tag.ListTag.add
import java.io.Serializable
import java.util.*
import javax.annotation.Nonnull
import kotlin.math.max

/**
 * Represents a version string allowing to compare.
 *
 *
 * The version must contains letters, numbers or ideograms. Other characters are considered separators.
 * If a string contains only separators than it will be considered the same as an empty string.
 *
 *
 * The versions are case insensitive but the original case sensitive version can be retrieved from [.toString].
 * @author joserobjr
 * @since 0.1.0
 */
class Version
/**
 * Creates a version object from the given String. The content will not be parsed, this operation is fast.
 * @param version The version string. Case insensitive.
 * @see Version
 *
 * @since 0.1.0
 */(@field:Nonnull @param:Nonnull private val original: String) : Comparable<Version>, Serializable {
    @Transient
    private var list: List<Comparable<*>>? = null

    @get:Nonnull
    val parts: List<Comparable<*>>
        /**
         * Returns the parts that was identified in the version string. It parses the version on the first call,
         * the next calls will return a cached result.
         * @return An immutable list containing a mix of [Integer] and [String] objects
         * @since 0.1.0
         */
        get() {
            if (list == null) {
                list = parse(original)
            }
            return list!!
        }

    /**
     * Compare this version with another to determine which one is newer.
     * Both version strings will be parsed if necessary and if they are not cached.
     * @param o The other version to compare
     * @throws NullPointerException If `o` is `null`
     * @throws ClassCastException If `o` is not an instance of [Version]
     * @return Negative number if this version is older than `o`, `0` if it is the same,
     * or a positive number if `o` is newer
     * @since 0.1.0
     */
    override fun compareTo(@Nonnull o: Version): Int {
        if (original.equals(o.original, ignoreCase = true)) {
            return 0
        }

        val partsB = o.parts
        val partsA = parts
        val sizeA = partsA.size
        val sizeB = partsB.size
        for (i in 0..<max(sizeA.toDouble(), sizeB.toDouble()).toInt()) {
            val a = if (i < sizeA) partsA[i] else 0
            val b = if (i < sizeB) partsB[i] else 0
            if (a.javaClass != b.javaClass) {
                return if (Int::class.java == a.javaClass) 1 else -1
            }
            val cmp = a.compareTo(b)
            if (cmp != 0) {
                return cmp
            }
        }
        return 0
    }

    /**
     * Returns the original string that was used to create this instance.
     * @return The original version string.
     * @since 0.1.0
     */
    override fun toString(): String {
        return original
    }

    /**
     * Compares if the other object is a [Version] object and it represents the same version.
     * The comparison is case insensitive.
     * Both version strings will be parsed if necessary and if they are not cached.
     * @param o The object to be compared
     * @return If the object represents the same version
     * @since 0.1.0
     */
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val other = o as Version
        if (original.equals(other.original, ignoreCase = true)) return true
        return compareTo(other) == 0
    }

    /**
     * Generates a hash code based on the processed parts.
     * The original string will be parsed if it was not parsed yet.
     * @return A hashcode compatible with the [.equals] and [.compareTo] implementations
     * @since 0.1.0
     */
    override fun hashCode(): Int {
        return parts.hashCode()
    }

    companion object {
        private const val serialVersionUID = 1L

        /**
         * Split the version into parts, separating the integers and the words from the version.
         * @param version The full version string
         * @return An immutable list containing [Integer] and [String] objects
         * @since 0.1.0
         */
        @Nonnull
        private fun parse(@Nonnull version: String): List<Comparable<*>> {
            val parts = ArrayList<Comparable<*>>(5)
            val len = version.length
            val pending = StringBuilder(len)
            var type: Byte
            var previous: Byte = 0
            for (i in 0..<len) {
                val c = version[i]
                type = if (c >= '0' && c <= '9') {
                    1
                } else if (Character.isDigit(c)) {
                    2
                } else if (Character.isLetter(c) || Character.isIdeographic(c.code)) {
                    3
                } else {
                    0
                }
                if (type != previous) {
                    addPendingPart(parts, pending, previous)
                    previous = type
                }
                when (type) {
                    1 -> pending.append(c)
                    2, 3 -> pending.append(c.lowercaseChar())
                    else -> {}
                }
            }
            addPendingPart(parts, pending, previous)
            parts.trimToSize()
            return Collections.unmodifiableList(parts)
        }

        /**
         * Adds an [Integer] or a [String] into `parts` built from the `pending` param
         * depending on the type given in the `type` param.
         * @param parts The list that will receive the object. Must support the [List.add] operation.
         * @param pending A builder containing the value of the object that will be added. The content will be cleared when used.
         * @param type One of the type bellow to indicate what is inside the `pending` param:
         *
         *  1. Contains only ASCII decimal digits
         *  1. Contains only numbers but they are not ASCII
         *  1. Contains letters and/or ideograms
         *
         * @since 0.1.0
         */
        private fun addPendingPart(
            @Nonnull parts: MutableList<Comparable<*>>,
            @Nonnull pending: StringBuilder,
            type: Byte
        ) {
            when (type) {
                1 -> {
                    parts.add(pending.toString().toInt())
                    pending.setLength(0)
                }

                2, 3 -> {
                    parts.add(pending.toString())
                    pending.setLength(0)
                }

                else -> {}
            }
        }
    }
}
