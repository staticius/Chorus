package org.chorus.level.format.bitarray

import io.netty.buffer.ByteBuf

class SingletonBitArray : BitArray {
    override fun set(index: Int, value: Int) {
        throw UnsupportedOperationException()
    }

    override fun get(index: Int): Int {
        return 0
    }

    override fun writeSizeToNetwork(buffer: ByteBuf, size: Int) {
        throw UnsupportedOperationException()
    }

    override fun size(): Int {
        return 1
    }

    override fun words(): IntArray {
        return EMPTY_ARRAY
    }

    override fun version(): BitArrayVersion {
        return BitArrayVersion.V0
    }

    override fun copy(): SingletonBitArray {
        return SingletonBitArray()
    }

    companion object {
        private val EMPTY_ARRAY = IntArray(0)
    }
}
