package org.chorus_oss.chorus.utils

import java.util.*

object StringUtils {
    fun beforeLast(str: String, splitter: String): String {
        val i = str.indexOf(splitter)
        if (i == -1) return str
        return str.substring(0, i)
    }

    fun afterFirst(str: String, splitter: String): String {
        val i = str.indexOf(splitter)
        if (i == -1) return str
        return str.substring(i + 1)
    }

    fun capitalize(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        if (str.length == 1) {
            return str.uppercase()
        }
        return str.substring(0, 1).uppercase() + str.substring(1)
    }

    /**
     * 在短字符串上(通常只有一个分割)处理比[String.split]快
     *
     *
     * Processing on short strings(There is usually only one split) is faster than [String.split]
     *
     * @param delimiter the delimiter
     * @param str       the str
     * @param limit     the limit
     * @return the list
     */
    @JvmOverloads
    fun fastSplit(delimiter: String, str: String, limit: Int = Int.MAX_VALUE): List<String> {
        var tmp = str
        val results = ArrayList<String>()
        var count = 1
        while (true) {
            val j = tmp.indexOf(delimiter)
            if (j < 0) {
                results.add(tmp)
                break
            }
            results.add(tmp.substring(0, j))
            count++
            tmp = tmp.substring(j + 1)
            if (count == limit || tmp.isEmpty()) {
                results.add(tmp)
                break
            }
        }
        return results
    }

    fun joinNotNull(delim: String, vararg elements: String?): String {
        val join = StringJoiner(delim)
        for (element in elements) {
            if (element != null) {
                join.add(element)
            }
        }
        return join.toString()
    }
}
