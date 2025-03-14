package org.chorus.level.format.bitarray

import io.netty.buffer.ByteBuf
import org.chorus.utils.ByteBufVarInt

interface BitArray {
    operator fun set(index: Int, value: Int)

    operator fun get(index: Int): Int

    fun writeSizeToNetwork(buffer: ByteBuf, size: Int) {
        ByteBufVarInt.writeInt(buffer, size)
    }

    fun readSizeFromNetwork(buffer: ByteBuf): Int {
        return ByteBufVarInt.readInt(buffer)
    }

    fun size(): Int

    fun words(): IntArray

    fun version(): BitArrayVersion

    fun copy(): BitArray
}