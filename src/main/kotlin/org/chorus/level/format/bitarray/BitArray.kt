package org.chorus.level.format.bitarray

import org.chorus.utils.ByteBufVarInt
import io.netty.buffer.ByteBuf

/**
 * Allay Project 2023/4/14
 *
 * @author JukeboxMC | daoge_cmd
 */
interface BitArray {
    fun set(index: Int, value: Int)

    fun get(index: Int): Int

    fun writeSizeToNetwork(buffer: ByteBuf?, size: Int) {
        ByteBufVarInt.writeInt(buffer, size)
    }

    fun readSizeFromNetwork(buffer: ByteBuf?): Int {
        return ByteBufVarInt.readInt(buffer)
    }

    fun size(): Int

    fun words(): IntArray

    fun version(): BitArrayVersion

    fun copy(): BitArray
}