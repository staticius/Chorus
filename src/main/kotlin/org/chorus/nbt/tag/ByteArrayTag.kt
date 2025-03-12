package org.chorus.nbt.tag

import org.chorus.utils.Binary

class ByteArrayTag : Tag<ByteArray> {
    var data: ByteArray? = null

    constructor()

    constructor(data: ByteArray?) {
        this.data = data
    }

    override val id: Byte
        get() = TAG_BYTE_ARRAY

    override fun toString(): String {
        return "ByteArrayTag " + " (data: 0x" + Binary.bytesToHexString(data, true) + " [" + data!!.size + " bytes])"
    }

    override fun toSNBT(): String {
        val builder = StringBuilder("[B;")
        for (i in 0..<data!!.size - 1) {
            builder.append(data!![i].toInt()).append('b').append(',')
        }
        builder.append(data!![data!!.size - 1].toInt()).append("b]")
        return builder.toString()
    }

    override fun toSNBT(space: Int): String {
        val builder = StringBuilder("[B; ")
        for (i in 0..<data!!.size - 1) {
            builder.append(data!![i].toInt()).append("b, ")
        }
        builder.append(data!![data!!.size - 1].toInt()).append("b]")
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) {
            val byteArrayTag = other as ByteArrayTag
            return ((data == null && byteArrayTag.data == null) || (data != null && data.contentEquals(byteArrayTag.data)))
        }
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun copy(): Tag<ByteArray> {
        val cp = ByteArray(data!!.size)
        System.arraycopy(data!!, 0, cp, 0, data!!.size)
        return ByteArrayTag(cp)
    }

    override fun parseValue(): ByteArray? {
        return this.data
    }
}
