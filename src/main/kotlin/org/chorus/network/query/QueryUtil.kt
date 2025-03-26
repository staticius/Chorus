package org.chorus.network.query

import io.netty.buffer.ByteBuf

import java.nio.charset.StandardCharsets

object QueryUtil {
    val LONG_RESPONSE_PADDING_TOP: ByteArray = byteArrayOf(115, 112, 108, 105, 116, 110, 117, 109, 0, -128, 0)
    val LONG_RESPONSE_PADDING_BOTTOM: ByteArray = byteArrayOf(1, 112, 108, 97, 121, 101, 114, 95, 0, 0)

    fun writeNullTerminatedByteArray(buf: ByteBuf, array: ByteArray?) {
        if (array != null) {
            buf.writeBytes(array)
        }
        buf.writeByte(0) // Null byte
    }

    fun readNullTerminatedString(`in`: ByteBuf): String {
        val read = StringBuilder()
        var readIn: Byte
        while ((`in`.readByte().also { readIn = it }) != '\u0000'.code.toByte()) {
            read.append(Char(readIn.toUShort()))
        }
        return read.toString()
    }

    fun writeNullTerminatedString(buf: ByteBuf, string: String) {
        writeNullTerminatedByteArray(buf, string.toByteArray(StandardCharsets.UTF_8))
    }
}
