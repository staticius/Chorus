package org.chorus.utils.collection.nb


object RangeUtil {
    fun checkPositiveOrZero(n: Int, name: String): Int {
        require(n >= 0) { "$name: $n (expected: >= 0)" }
        return n
    }
}
