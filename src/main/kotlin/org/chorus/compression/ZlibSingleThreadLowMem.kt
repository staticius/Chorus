package org.chorus.compression

import org.chorus.utils.ThreadCache
import java.io.IOException
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater

class ZlibSingleThreadLowMem : ZlibProvider {
    @Synchronized
    @Throws(IOException::class)
    override fun deflate(data: ByteArray, level: Int, raw: Boolean): ByteArray? {
        if (raw) {
            DEFLATER_RAW.reset()
            DEFLATER_RAW.setLevel(level)
            DEFLATER_RAW.setInput(data)
            DEFLATER_RAW.finish()
            val bos = ThreadCache.fbaos.get()
            bos.reset()
            while (!DEFLATER_RAW.finished()) {
                val i = DEFLATER_RAW.deflate(BUFFER)
                bos.write(BUFFER, 0, i)
            }
            return bos.toByteArray()
        } else {
            DEFLATER.reset()
            DEFLATER.setLevel(level)
            DEFLATER.setInput(data)
            DEFLATER.finish()
            val bos = ThreadCache.fbaos.get()
            bos.reset()
            while (!DEFLATER.finished()) {
                val i = DEFLATER.deflate(BUFFER)
                bos.write(BUFFER, 0, i)
            }
            return bos.toByteArray()
        }
    }

    @Synchronized
    @Throws(IOException::class)
    override fun inflate(data: ByteArray, maxSize: Int, raw: Boolean): ByteArray? {
        if (raw) {
            INFLATER_RAW.reset()
            INFLATER_RAW.setInput(data)
            INFLATER_RAW.finished()
            val bos = ThreadCache.fbaos.get()
            bos.reset()
            try {
                var length = 0
                while (!INFLATER_RAW.finished()) {
                    val i = INFLATER_RAW.inflate(BUFFER)
                    length += i
                    if (maxSize > 0 && length > maxSize) {
                        throw IOException("Inflated data exceeds maximum size")
                    }
                    bos.write(BUFFER, 0, i)
                }
                return bos.toByteArray()
            } catch (e: DataFormatException) {
                throw IOException("Unable to inflate zlib stream", e)
            }
        } else {
            INFLATER.reset()
            INFLATER.setInput(data)
            INFLATER.finished()
            val bos = ThreadCache.fbaos.get()
            bos.reset()
            try {
                var length = 0
                while (!INFLATER.finished()) {
                    val i = INFLATER.inflate(BUFFER)
                    length += i
                    if (maxSize > 0 && length > maxSize) {
                        throw IOException("Inflated data exceeds maximum size")
                    }
                    bos.write(BUFFER, 0, i)
                }
                return bos.toByteArray()
            } catch (e: DataFormatException) {
                throw IOException("Unable to inflate zlib stream", e)
            }
        }
    }

    companion object {
        private const val BUFFER_SIZE = 8192
        private val DEFLATER = Deflater(Deflater.BEST_COMPRESSION)
        private val DEFLATER_RAW = Deflater(Deflater.BEST_COMPRESSION, true)
        private val INFLATER = Inflater()
        private val INFLATER_RAW = Inflater(true)
        private val BUFFER = ByteArray(BUFFER_SIZE)
    }
}
