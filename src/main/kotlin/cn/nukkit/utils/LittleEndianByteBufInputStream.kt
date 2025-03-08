package cn.nukkit.utils

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class LittleEndianByteBufInputStream(private val buffer: ByteBuf) : ByteBufInputStream(buffer) {
    @Throws(IOException::class)
    override fun readChar(): Char {
        return Character.reverseBytes(buffer.readChar())
    }

    @Throws(IOException::class)
    override fun readDouble(): Double {
        return buffer.readDoubleLE()
    }

    @Throws(IOException::class)
    override fun readFloat(): Float {
        return buffer.readFloatLE()
    }

    @Throws(IOException::class)
    override fun readShort(): Short {
        return buffer.readShortLE()
    }

    @Throws(IOException::class)
    override fun readUnsignedShort(): Int {
        return buffer.readUnsignedShortLE()
    }

    @Throws(IOException::class)
    override fun readLong(): Long {
        return buffer.readLongLE()
    }

    @Throws(IOException::class)
    override fun readInt(): Int {
        return buffer.readIntLE()
    }

    @Throws(IOException::class)
    override fun readUTF(): String {
        val length = readUnsignedShort()
        return buffer.readCharSequence(length, StandardCharsets.UTF_8) as String
    }
}
