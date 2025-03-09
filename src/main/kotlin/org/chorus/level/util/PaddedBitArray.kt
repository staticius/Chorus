package org.chorus.level.util

import org.chorus.math.MathHelper
import com.google.common.base.Preconditions

class PaddedBitArray internal constructor(
    /**
     * Palette version information
     */
    override val version: BitArrayVersion,
    /**
     * Number of entries in this palette (**not** the length of the words array that internally backs this palette)
     */
    private val size: Int,
    /**
     * Array used to store data
     */
    override val words: IntArray
) :
    BitArray {
    init {
        val expectedWordsLength = MathHelper.ceil(size.toFloat() / version.entriesPerWord)
        require(words.size == expectedWordsLength) {
            "Invalid length given for storage, got: " + words.size +
                    " but expected: " + expectedWordsLength
        }
    }

    override fun set(index: Int, value: Int) {
        Preconditions.checkElementIndex(index, this.size)
        Preconditions.checkArgument(
            value >= 0 && value <= version.maxEntryValue,
            "Max value: %s. Received value", version.maxEntryValue, value
        )
        val arrayIndex = index / version.entriesPerWord
        val offset = (index % version.entriesPerWord) * version.bits

        words[arrayIndex] =
            words[arrayIndex] and (version.maxEntryValue shl offset).inv() or ((value and version.maxEntryValue) shl offset)
    }

    override fun get(index: Int): Int {
        Preconditions.checkElementIndex(index, this.size)
        val arrayIndex = index / version.entriesPerWord
        val offset = (index % version.entriesPerWord) * version.bits

        return (words[arrayIndex] ushr offset) and version.maxEntryValue
    }

    override fun size(): Int {
        return this.size
    }

    override fun copy(): BitArray {
        return PaddedBitArray(
            this.version, this.size,
            words.copyOf(words.size)
        )
    }
}
