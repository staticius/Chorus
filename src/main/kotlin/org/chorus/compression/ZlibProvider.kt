package org.chorus.compression

import java.io.IOException

/**
 * @author ScraMTeam
 */
interface ZlibProvider {
    @Throws(IOException::class)
    fun deflate(data: ByteArray, level: Int, raw: Boolean): ByteArray?

    @Throws(IOException::class)
    fun inflate(data: ByteArray, maxSize: Int, raw: Boolean): ByteArray?
}
