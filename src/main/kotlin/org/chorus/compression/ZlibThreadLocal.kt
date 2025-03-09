package org.chorus.compression

import org.chorus.utils.ThreadCache
import java.io.IOException
import java.util.function.Supplier
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater

class ZlibThreadLocal : ZlibProvider {
    @Throws(IOException::class)
    override fun deflate(data: ByteArray, level: Int, raw: Boolean): ByteArray? {
        val deflater = if (raw) DEFLATER_RAW.get() else DEFLATER.get()
        val bos = ThreadCache.fbaos.get()
        try {
            deflater.reset()
            deflater.setLevel(level)
            deflater.setInput(data)
            deflater.finish()
            bos.reset()
            val buffer = BUFFER.get()
            while (!deflater.finished()) {
                val i = deflater.deflate(buffer)
                bos.write(buffer, 0, i)
            }
        } finally {
            deflater.reset()
        }
        //Deflater::end is called the time when the process exits.
        return bos.toByteArray()
    }

    @Throws(IOException::class)
    override fun inflate(data: ByteArray, maxSize: Int, raw: Boolean): ByteArray? {
        val inflater = if (raw) INFLATER_RAW.get() else INFLATER.get()
        try {
            inflater.reset()
            inflater.setInput(data)
            inflater.finished()
            val bos = ThreadCache.fbaos.get()
            bos.reset()

            val buffer = BUFFER.get()
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
        } finally {
            inflater.reset()
        }
    }

    companion object {
        private val INFLATER: ThreadLocal<Inflater> = ThreadLocal.withInitial<Inflater>(
            Supplier<Inflater> { Inflater() })
        private val DEFLATER: ThreadLocal<Deflater> = ThreadLocal.withInitial<Deflater>(
            Supplier<Deflater> { Deflater() })
        private val INFLATER_RAW: ThreadLocal<Inflater> = ThreadLocal.withInitial {
            Inflater(
                true
            )
        }
        private val DEFLATER_RAW: ThreadLocal<Deflater> = ThreadLocal.withInitial {
            Deflater(
                -1,
                true
            )
        }
        private val BUFFER: ThreadLocal<ByteArray> = ThreadLocal.withInitial {
            ByteArray(
                8192
            )
        }
    }
}
