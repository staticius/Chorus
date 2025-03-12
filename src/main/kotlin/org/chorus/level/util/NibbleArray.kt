package org.chorus.level.util

import com.google.common.base.Preconditions
import org.chorus.utils.collection.ByteArrayWrapper
import org.chorus.utils.collection.FreezableArrayManager
import java.util.*

class NibbleArray : Cloneable {
    private val length: Int
    private val byteArrayWrapper: ByteArrayWrapper

    constructor(length: Int) {
        byteArrayWrapper = FreezableArrayManager.getInstance().createByteArray(length / 2)
        this.length = length / 2
    }

    constructor(array: ByteArray) {
        byteArrayWrapper = FreezableArrayManager.getInstance().wrapByteArray(array)
        this.length = array.size
    }

    operator fun get(index: Int): Byte {
        Preconditions.checkElementIndex(index, length * 2)
        val `val` = byteArrayWrapper.getByte(index / 2)
        return if ((index and 1) == 0) {
            (`val`.toInt() and 0x0f).toByte()
        } else {
            ((`val`.toInt() and 0xf0) ushr 4).toByte()
        }
    }

    operator fun set(index: Int, value: Byte) {
        var value = value
        Preconditions.checkArgument(value >= 0 && value < 16, "Nibbles must have a value between 0 and 15.")
        Preconditions.checkElementIndex(index, length * 2)
        value = (value.toInt() and 0xf).toByte()
        val half = index / 2
        val previous = byteArrayWrapper.getByte(half)
        if ((index and 1) == 0) {
            byteArrayWrapper.setByte(half, (previous.toInt() and 0xf0 or value.toInt()).toByte())
        } else {
            byteArrayWrapper.setByte(half, (previous.toInt() and 0x0f or (value.toInt() shl 4)).toByte())
        }
    }

    fun fill(value: Byte) {
        var value = value
        Preconditions.checkArgument(value >= 0 && value < 16, "Nibbles must have a value between 0 and 15.")
        value = (value.toInt() and 0xf).toByte()
        val rawBytes = data
        Arrays.fill(rawBytes, ((value.toInt() shl 4) or value.toInt()).toByte())
        byteArrayWrapper.rawBytes = rawBytes
    }

    fun copyFrom(bytes: ByteArray) {
        val rawBytes = data
        Preconditions.checkNotNull(bytes, "bytes")
        Preconditions.checkArgument(
            bytes.size == rawBytes.size, "length of provided byte array is %s but expected %s", bytes.size,
            rawBytes.size
        )
        System.arraycopy(bytes, 0, rawBytes, 0, rawBytes.size)
        byteArrayWrapper.rawBytes = rawBytes
    }

    fun copyFrom(array: NibbleArray) {
        Preconditions.checkNotNull(array, "array")
        copyFrom(array.data)
    }

    val data: ByteArray
        get() = byteArrayWrapper.rawBytes

    fun copy(): NibbleArray {
        return NibbleArray(data.clone())
    }

    public override fun clone(): NibbleArray {
        try {
            val clone = super.clone() as NibbleArray
            clone.copyFrom(this)
            return clone
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }
}
