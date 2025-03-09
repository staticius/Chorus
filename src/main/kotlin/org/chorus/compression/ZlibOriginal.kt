package org.chorus.compression

import cn.nukkit.utils.ThreadCache
import java.io.IOException
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater

class ZlibOriginal : ZlibProvider {
    @Throws(IOException::class)
    override fun deflate(data: ByteArray, level: Int, raw: Boolean): ByteArray? {
        val deflater = Deflater(level, raw)
        deflater.setInput(data)
        deflater.finish()
        val bos = ThreadCache.fbaos.get()
        bos.reset()
        val buf = ByteArray(1024)
        try {
            while (!deflater.finished()) {
                val i = deflater.deflate(buf)
                bos.write(buf, 0, i)
            }
        } finally {
            deflater.end()
        }
        return bos.toByteArray()
    }

    @Throws(IOException::class)
    override fun inflate(data: ByteArray, maxSize: Int, raw: Boolean): ByteArray? {
        val inflater = Inflater(raw)
        inflater.setInput(data)
        inflater.finished()
        val bos = ThreadCache.fbaos.get()
        bos.reset()

        val buffer = ByteArray(1024)
        try {
            var length = 0
            while (!inflater.finished()) {
                val i = inflater.inflate(buffer)
                length += i
                if (maxSize > 0 && length > maxSize) {
                    throw IOException("Inflated data exceeds maximum size")
                }
                bos.write(buffer, 0, i)
            }
            return bos.toByteArray()
        } catch (e: DataFormatException) {
            throw IOException("Unable to inflate zlib stream", e)
        }
    }
}
