package org.chorus.utils

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.ByteBufUtil
import java.io.IOException
import java.nio.charset.StandardCharsets

class LittleEndianByteBufOutputStream(private val buffer: ByteBuf) : ByteBufOutputStream(buffer) {
    @Throws(IOException::class)
    override fun writeChar(v: Int) {
        buffer.writeChar(Character.reverseBytes(v.toChar()).code)
    }

    @Throws(IOException::class)
    override fun writeDouble(v: Double) {
        buffer.writeDoubleLE(v)
    }

    @Throws(IOException::class)
    override fun writeFloat(v: Float) {
        buffer.writeFloatLE(v)
    }

    @Throws(IOException::class)
    override fun writeShort(`val`: Int) {
        buffer.writeShortLE(`val`)
    }

    @Throws(IOException::class)
    override fun writeLong(`val`: Long) {
        buffer.writeLongLE(`val`)
    }

    @Throws(IOException::class)
    override fun writeInt(`val`: Int) {
        buffer.writeIntLE(`val`)
    }

    @Throws(IOException::class)
    override fun writeUTF(string: String) {
        val length = ByteBufUtil.utf8Bytes(string)
        this.writeShort(length)
        buffer.writeCharSequence(string, StandardCharsets.UTF_8)
    }
}
