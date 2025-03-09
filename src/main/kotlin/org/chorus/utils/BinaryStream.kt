package org.chorus.utils

import cn.nukkit.nbt.NBTIO.read
import cn.nukkit.nbt.NBTIO.write
import cn.nukkit.nbt.tag.CompoundTag
import io.netty.util.internal.EmptyArrays
import lombok.SneakyThrows
import lombok.extern.slf4j.Slf4j
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.*
import java.util.function.Function
import kotlin.math.min

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Slf4j
class BinaryStream {
    var offset: Int
    private var buffer: ByteArray?
    var count: Int
        protected set

    constructor() {
        this.buffer = ByteArray(32)
        this.offset = 0
        this.count = 0
    }

    @JvmOverloads
    constructor(buffer: ByteArray, offset: Int = 0) {
        this.buffer = buffer
        this.offset = offset
        this.count = buffer.size
    }

    fun reset(): BinaryStream {
        this.offset = 0
        this.count = 0
        return this
    }

    fun setBuffer(buffer: ByteArray?) {
        this.buffer = buffer
        this.count = buffer?.size ?: -1
    }

    fun setBuffer(buffer: ByteArray?, offset: Int) {
        this.setBuffer(buffer)
        this.offset = offset
    }

    fun getBuffer(): ByteArray {
        return buffer!!.copyOf(count)
    }

    @JvmOverloads
    fun get(len: Int = this.count - this.offset): ByteArray {
        var len = len
        if (len < 0) {
            this.offset = this.count - 1
            return EmptyArrays.EMPTY_BYTES
        }
        len = min(len.toDouble(), (this.count - this.offset).toDouble()).toInt()
        this.offset += len
        return Arrays.copyOfRange(this.buffer, this.offset - len, this.offset)
    }

    fun put(bytes: ByteArray?) {
        if (bytes == null) {
            return
        }

        this.ensureCapacity(this.count + bytes.size)

        System.arraycopy(bytes, 0, this.buffer, this.count, bytes.size)
        this.count += bytes.size
    }

    val long: Long
        get() = Binary.Companion.readLong(this.get(8))

    fun putLong(l: Long) {
        this.put(Binary.Companion.writeLong(l))
    }

    val int: Int
        get() = Binary.Companion.readInt(this.get(4))

    fun putInt(i: Int) {
        this.put(Binary.Companion.writeInt(i))
    }

    fun putMedium(i: Int) {
        putByte((i ushr 16).toByte())
        putByte((i ushr 8).toByte())
        putByte(i.toByte())
    }

    val medium: Int
        get() {
            var value = (byte.toInt() and 0xff) shl 16 or (
                    (byte.toInt() and 0xff) shl 8) or (
                    byte.toInt() and 0xff)
            if ((value and 0x800000) != 0) {
                value = value or -0x1000000
            }
            return value
        }

    val lLong: Long
        get() = Binary.Companion.readLLong(this.get(8))

    fun putLLong(l: Long) {
        this.put(Binary.Companion.writeLLong(l))
    }

    val lInt: Int
        get() = Binary.Companion.readLInt(this.get(4))

    fun putLInt(i: Int) {
        this.put(Binary.Companion.writeLInt(i))
    }

    fun <T> putNotNull(data: T?, consumer: Consumer<T?>) {
        val present = data != null
        putBoolean(present)
        if (present) {
            consumer.accept(data)
        }
    }

    fun <T> putOptional(data: OptionalValue<T>, consumer: Consumer<T?>) {
        val present = data.isPresent
        putBoolean(present)
        if (present) {
            consumer.accept(data.get())
        }
    }

    val short: Int
        get() = Binary.Companion.readShort(this.get(2))

    fun putShort(s: Int) {
        this.put(Binary.Companion.writeShort(s))
    }

    val lShort: Int
        get() = Binary.Companion.readLShort(this.get(2))

    fun putLShort(s: Int) {
        this.put(Binary.Companion.writeLShort(s))
    }

    val float: Float
        get() = getFloat(-1)

    fun getFloat(accuracy: Int): Float {
        return Binary.Companion.readFloat(this.get(4), accuracy)
    }

    fun putFloat(v: Float) {
        this.put(Binary.Companion.writeFloat(v))
    }

    val lFloat: Float
        get() = getLFloat(-1)

    fun getLFloat(accuracy: Int): Float {
        return Binary.Companion.readLFloat(this.get(4), accuracy)
    }

    fun putLFloat(v: Float) {
        this.put(Binary.Companion.writeLFloat(v))
    }

    val triad: Int
        get() = Binary.Companion.readTriad(this.get(3))

    fun putTriad(triad: Int) {
        this.put(Binary.Companion.writeTriad(triad))
    }

    val lTriad: Int
        get() = Binary.Companion.readLTriad(this.get(3))

    fun putLTriad(triad: Int) {
        this.put(Binary.Companion.writeLTriad(triad))
    }

    val boolean: Boolean
        get() = byte.toInt() == 0x01

    fun putBoolean(bool: Boolean) {
        this.putByte((if (bool) 1 else 0).toByte())
    }

    val byte: Byte
        get() = (buffer!![offset++].toInt() and 0xff).toByte()

    fun putByte(b: Byte) {
        this.put(byteArrayOf(b))
    }

    val byteArray: ByteArray
        get() = this.get(unsignedVarInt.toInt())

    fun putByteArray(b: ByteArray) {
        this.putUnsignedVarInt(b.size.toLong())
        this.put(b)
    }

    val string: String
        get() = String(this.byteArray, StandardCharsets.UTF_8)

    fun putString(string: String) {
        val b = string.toByteArray(StandardCharsets.UTF_8)
        this.putByteArray(b)
    }

    val unsignedVarInt: Long
        get() = VarInt.readUnsignedVarInt(this)

    fun putUnsignedVarInt(v: Long) {
        VarInt.writeUnsignedVarInt(this, v)
    }

    val varInt: Int
        get() = VarInt.readVarInt(this)

    fun putVarInt(v: Int) {
        VarInt.writeVarInt(this, v)
    }

    val varLong: Long
        get() = VarInt.readVarLong(this)

    fun putVarLong(v: Long) {
        VarInt.writeVarLong(this, v)
    }

    val unsignedVarLong: Long
        get() = VarInt.readUnsignedVarLong(this)

    fun putUnsignedVarLong(v: Long) {
        VarInt.writeUnsignedVarLong(this, v)
    }

    fun <T> putArray(collection: Collection<T>?, writer: Consumer<T>?) {
        if (collection == null) {
            putUnsignedVarInt(0)
            return
        }
        putUnsignedVarInt(collection.size.toLong())
        collection.forEach(writer)
    }

    fun <T> putArray(collection: Array<T>?, writer: Consumer<T>) {
        if (collection == null) {
            putUnsignedVarInt(0)
            return
        }
        putUnsignedVarInt(collection.size.toLong())
        for (t in collection) {
            writer.accept(t)
        }
    }

    fun <T> putArray(array: Collection<T>, biConsumer: BiConsumer<BinaryStream?, T>) {
        this.putUnsignedVarInt(array.size.toLong())
        for (`val` in array) {
            biConsumer.accept(this, `val`)
        }
    }

    fun <T> getArray(clazz: Class<T>?, function: Function<BinaryStream?, T>): Array<T> {
        val deque = ArrayDeque<T>()
        val count = unsignedVarInt.toInt()
        for (i in 0..<count) {
            deque.add(function.apply(this))
        }
        return deque.toArray(java.lang.reflect.Array.newInstance(clazz, 0) as Array<T>)
    }

    fun <T> getArray(array: MutableCollection<T>, function: Function<BinaryStream?, T>) {
        getArray(
            array,
            { obj: BinaryStream -> obj.unsignedVarInt }, function
        )
    }

    fun <T> getArray(
        array: MutableCollection<T>,
        lengthReader: ToLongFunction<BinaryStream>,
        function: Function<BinaryStream?, T>
    ) {
        val length = lengthReader.applyAsLong(this)
        for (i in 0..<length) {
            array.add(function.apply(this))
        }
    }

    fun feof(): Boolean {
        return this.offset < 0 || this.offset >= buffer!!.size
    }

    @get:SneakyThrows(IOException::class)
    val tag: CompoundTag
        get() {
            val `is` = ByteArrayInputStream(buffer, offset, buffer!!.size)
            val initial = `is`.available()
            try {
                return read(`is`)
            } finally {
                offset += initial - `is`.available()
            }
        }

    @SneakyThrows(IOException::class)
    fun putTag(tag: CompoundTag) {
        put(write(tag))
    }

    private fun ensureCapacity(minCapacity: Int) {
        // overflow-conscious code
        if (minCapacity - buffer!!.size > 0) {
            grow(minCapacity)
        }
    }

    private fun grow(minCapacity: Int) {
        // overflow-conscious code
        val oldCapacity = buffer!!.size
        var newCapacity = oldCapacity shl 1

        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity)
        }
        this.buffer = buffer!!.copyOf(newCapacity)
    }

    companion object {
        private const val MAX_ARRAY_SIZE = Int.MAX_VALUE - 8

        private fun hugeCapacity(minCapacity: Int): Int {
            if (minCapacity < 0) { // overflow
                throw OutOfMemoryError()
            }
            return if (minCapacity > MAX_ARRAY_SIZE) Int.MAX_VALUE else MAX_ARRAY_SIZE
        }
    }
}
