package org.chorus.utils

import org.chorus.nbt.tag.ListTag.add

import kotlin.math.min


class HumanStringComparator : Comparator<String> {
    override fun compare(o1: String, o2: String): Int {
        if (o1 == o2) {
            return EQUALS
        }

        val l1 = splitSymbols(combineNegativeSign(split(o1)))
        val l2 = splitSymbols(combineNegativeSign(split(o2)))

        return compare(l1, l2)
    }

    private fun splitSymbols(list: List<String>): List<String> {
        var changed = false
        var result = list
        val size = list.size
        for (i in size - 1 downTo 0) {
            val str = result[i]
            val length = str.length
            var lastPart = length
            for (j in length - 1 downTo 0) {
                val c = str[j]
                if (SYMBOLS.indexOf(c) != -1) {
                    if (!changed) {
                        result = if (list is ArrayList<*>) list else ArrayList(list)
                        changed = true
                    }
                    val indexToAddLast: Int
                    if (j > 0) {
                        result.set(i, str.substring(0, j))
                        result.add(i + 1, c.toString())
                        indexToAddLast = i + 2
                    } else {
                        result.set(i, c.toString())
                        indexToAddLast = i + 1
                    }
                    if (j + 2 <= length) {
                        result.add(indexToAddLast, str.substring(j + 1, lastPart))
                    }
                    lastPart = j
                }
            }
        }
        return result
    }

    private fun compare(l1: List<String>, l2: List<String>): Int {
        val len1 = l1.size
        val len2 = l2.size
        val minLen = min(len1.toDouble(), len2.toDouble()).toInt()
        for (i in 0..<minLen) {
            val str1 = l1[i]
            val str2 = l2[i]
            val strLen1 = str1.length
            val strLen2 = str2.length
            assert(strLen1 > 0)
            assert(strLen2 > 0)
            val isNum1 = Character.isDigit(str1[strLen1 - 1])
            val isNum2 = Character.isDigit(str2[strLen2 - 1])
            if (isNum1) {
                if (isNum2) {
                    val i1 = str1.toInt()
                    val i2 = str2.toInt()
                    var result = Integer.compare(i1, i2)
                    if (result != EQUALS) {
                        return result
                    }
                    // Number with higher 0 padding goes before
                    result = Integer.compare(strLen1, strLen2)
                    if (result != EQUALS) {
                        return result
                    }
                } else {
                    return RIGHT
                }
            } else if (isNum2) {
                return LEFT
            } else {
                if (strLen1 == strLen2) {
                    val result = str1.compareTo(str2)
                    if (result != EQUALS) {
                        return result
                    }
                } else {
                    val minStrLen = min(strLen1.toDouble(), strLen2.toDouble()).toInt()
                    val commonPart1 = str1.substring(0, minStrLen)
                    val commonPart2 = str2.substring(0, minStrLen)
                    val result = commonPart1.compareTo(commonPart2)
                    if (result != EQUALS) {
                        return result
                    }

                    // Detect omitted number
                    if (strLen1 < strLen2) {
                        if (detectOmittedNumber(l1, len1, i, str2, strLen2, minStrLen, commonPart1)) {
                            return RIGHT
                        }
                    } else if (detectOmittedNumber(l2, len2, i, str1, strLen1, minStrLen, commonPart2)) {
                        return LEFT
                    }

                    return Integer.compare(strLen1, strLen2)
                }
            }
        }

        return Integer.compare(len1, len2)
    }

    private fun detectOmittedNumber(
        l1: List<String>,
        len1: Int,
        i: Int,
        str2: String,
        strLen2: Int,
        minStrLen: Int,
        commonPart1: String
    ): Boolean {
        val combined: String
        val comparingWith: String
        val next1 = if (len1 > i + 1) l1[i + 1] else null
        val nextLen1 = next1?.length ?: 0
        val isDigit1 = next1 != null && Character.isDigit(next1[nextLen1 - 1])
        val afterNext1 = if (isDigit1 && len1 > i + 2) l1[i + 2] else null
        val afterNextLen1 = afterNext1?.length ?: 0
        if (afterNextLen1 > 0) {
            combined = commonPart1 + afterNext1!!.substring(
                0,
                min(afterNextLen1.toDouble(), (strLen2 - minStrLen).toDouble()).toInt()
            )
            comparingWith = str2
            return combined == comparingWith
        }
        return false
    }

    private fun combineNegativeSign(list: List<String>): List<String> {
        val size = list.size
        if (size < 2) {
            return list
        }

        var i = size - 1
        while (i > 0) {
            val str1 = list[i]
            val strLen1 = str1.length
            if (strLen1 > 0 && Character.isDigit(str1[strLen1 - 1])) {
                val str2 = list[i - 1]
                val strLen2 = str2.length
                if (strLen2 > 0 && str2[strLen2 - 1] == '-') {
                    list.set(i, "-$str1")
                    if (strLen2 == 1) {
                        list.removeAt(i - 1)
                        i -= 2
                    } else {
                        list.set(i - 1, str2.substring(0, strLen2 - 1))
                        i--
                    }
                }
            }
            i--
        }

        return list
    }

    private fun split(str: String): List<String> {
        val length = str.length
        if (length == 0) {
            return emptyList()
        } else if (length == 1) {
            return listOf(str)
        }

        var list: List<String>? = null
        var wasDigit = false
        var start = -1
        for (i in 0..<length) {
            if (Character.isDigit(str[i])) {
                if (!wasDigit && start == -1) {
                    start = i
                    wasDigit = true
                } else if (!wasDigit) {
                    if (list == null) {
                        list = ArrayList(2)
                    }
                    list.add(str.substring(start, i))
                    start = i
                    wasDigit = true
                }
            } else {
                if (wasDigit) {
                    if (list == null) {
                        list = ArrayList(2)
                    }
                    list.add(str.substring(start, i))
                    start = i
                    wasDigit = false
                } else if (start == -1) {
                    start = i
                }
            }
        }

        val substring = str.substring(start, length)
        if (list == null) {
            list = listOf(substring)
        } else {
            list.add(substring)
        }

        return list
    }

    companion object {
        val instance: HumanStringComparator = HumanStringComparator()
        private const val LEFT = -1
        private const val RIGHT = 1
        private const val EQUALS = 0
        private const val SYMBOLS = "[:.;,/\\]{}|="
    }
}
