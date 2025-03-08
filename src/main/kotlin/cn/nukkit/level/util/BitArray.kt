package cn.nukkit.level.util

interface BitArray {
    fun set(index: Int, value: Int)

    fun get(index: Int): Int

    fun size(): Int

    val words: IntArray

    val version: BitArrayVersion

    fun copy(): BitArray
}
