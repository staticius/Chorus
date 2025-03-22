package org.chorus.level.format.bitarray

import com.google.common.base.Objects
import org.chorus.math.ChorusMath

@JvmRecord
data class PaddedBitArray(val version: BitArrayVersion, val size: Int, val words: IntArray) : BitArray {
    override fun size(): Int {
        return size
    }

    override fun words(): IntArray {
        return words
    }

    override fun version(): BitArrayVersion {
        return version
    }

    override fun set(index: Int, value: Int) {
        val arrayIndex = index / version.entriesPerWord
        val offset = (index % version.entriesPerWord) * version.bits
        words[arrayIndex] =
            words[arrayIndex] and (version.maxEntryValue shl offset).inv() or ((value and version.maxEntryValue) shl offset)
    }

    override fun get(index: Int): Int {
        val arrayIndex = index / version.entriesPerWord
        val offset = (index % version.entriesPerWord) * version.bits
        return (words[arrayIndex] ushr offset) and version.maxEntryValue
    }

    override fun copy(): BitArray {
        return PaddedBitArray(
            this.version, this.size,
            words.copyOf(words.size)
        )
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is PaddedBitArray) return false
        return size == o.size && version == o.version && words.contentEquals(o.words)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(version, size, words.contentHashCode())
    }

    init {
        val expectedWordsLength = ChorusMath.ceilFloat(size.toFloat() / version.entriesPerWord)
        require(words.size == expectedWordsLength) {
            "Invalid length given for storage, got: " + words.size +
                    " but expected: " + expectedWordsLength
        }
    }
}
