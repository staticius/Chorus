package org.chorus.utils

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SeekableByteChannel
import java.util.concurrent.atomic.AtomicBoolean

class SeekableInMemoryByteChannel @JvmOverloads constructor(data: ByteArray = ByteArray(0)) :
    SeekableByteChannel {
    private var data: ByteArray
    private val closed = AtomicBoolean()
    private var position = 0
    private var size: Int

    init {
        this.data = data
        this.size = data.size
    }

    constructor(size: Int) : this(ByteArray(size))

    override fun position(): Long {
        return position.toLong()
    }

    @Throws(IOException::class)
    override fun position(newPosition: Long): SeekableByteChannel {
        this.ensureOpen()
        if (newPosition in 0L..2147483647L) {
            this.position = newPosition.toInt()
            return this
        } else {
            throw IOException("Position has to be in range 0.. 2147483647")
        }
    }

    override fun size(): Long {
        return size.toLong()
    }

    override fun truncate(newSize: Long): SeekableByteChannel {
        if (newSize in 0L..2147483647L) {
            if (size.toLong() > newSize) {
                this.size = newSize.toInt()
            }

            if (position.toLong() > newSize) {
                this.position = newSize.toInt()
            }

            return this
        } else {
            throw IllegalArgumentException("Size has to be in range 0.. 2147483647")
        }
    }

    @Throws(IOException::class)
    override fun read(buf: ByteBuffer): Int {
        this.ensureOpen()
        var wanted = buf.remaining()
        val possible = this.size - this.position
        if (possible <= 0) {
            return -1
        } else {
            if (wanted > possible) {
                wanted = possible
            }

            buf.put(this.data, this.position, wanted)
            this.position += wanted
            return wanted
        }
    }

    override fun close() {
        closed.set(true)
    }

    override fun isOpen(): Boolean {
        return !closed.get()
    }

    @Throws(IOException::class)
    override fun write(b: ByteBuffer): Int {
        this.ensureOpen()
        var wanted = b.remaining()
        val possibleWithoutResize = this.size - this.position
        if (wanted > possibleWithoutResize) {
            val newSize = this.position + wanted
            if (newSize < 0) {
                this.resize(2147483647)
                wanted = 2147483647 - this.position
            } else {
                this.resize(newSize)
            }
        }

        b[data, position, wanted]
        this.position += wanted
        if (this.size < this.position) {
            this.size = this.position
        }

        return wanted
    }

    fun array(): ByteArray {
        return this.data
    }

    private fun resize(newLength: Int) {
        var len = data.size
        if (len <= 0) {
            len = 1
        }

        if (newLength < 1073741823) {
            while (len < newLength) {
                len = len shl 1
            }
        } else {
            len = newLength
        }

        this.data = data.copyOf(len)
    }

    @Throws(ClosedChannelException::class)
    private fun ensureOpen() {
        if (!this.isOpen) {
            throw ClosedChannelException()
        }
    }

    companion object {
        private const val NAIVE_RESIZE_LIMIT = 1073741823
    }
}
