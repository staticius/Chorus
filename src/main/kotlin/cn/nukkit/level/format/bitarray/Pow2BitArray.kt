package cn.nukkit.level.format.bitarray

import cn.nukkit.math.NukkitMath
import com.google.common.base.Objects

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
@JvmRecord
data class Pow2BitArray(val version: BitArrayVersion, val size: Int, val words: IntArray) :
    BitArray {
    override fun set(index: Int, value: Int) {
        val bitIndex = index * version.bits
        val arrayIndex = bitIndex shr 5
        val offset = bitIndex and 31
        words[arrayIndex] =
            words[arrayIndex] and (version.maxEntryValue shl offset).inv() or ((value and version.maxEntryValue) shl offset)
    }

    override fun get(index: Int): Int {
        val bitIndex = index * version.bits
        val arrayIndex = bitIndex shr 5
        val wordOffset = bitIndex and 31
        return words[arrayIndex] ushr wordOffset and version.maxEntryValue
    }

    override fun copy(): BitArray {
        return Pow2BitArray(
            this.version, this.size,
            words.copyOf(words.size)
        )
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Pow2BitArray) return false
        return size == o.size && version == o.version && words.contentEquals(o.words)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(version, size, words.contentHashCode())
    }

    init {
        val expectedWordsLength = NukkitMath.ceilFloat(size.toFloat() / version.entriesPerWord)
        require(words.size == expectedWordsLength) {
            "Invalid length given for storage, got: " + words.size +
                    " but expected: " + expectedWordsLength
        }
    }
}