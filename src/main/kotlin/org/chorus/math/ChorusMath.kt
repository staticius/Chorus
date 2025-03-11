package org.chorus.math

import org.chorus.utils.random.NukkitRandom
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


object ChorusMath {
    private const val ZERO_BYTE: Byte = 0
    private const val ZERO_INTEGER = 0
    private const val ZERO_SHORT: Short = 0
    private const val ZERO_LONG = 0L


    fun isZero(storage: Number): Boolean {
        return ZERO_BYTE == storage
                || ZERO_INTEGER == storage
                || ZERO_SHORT == storage
                || ZERO_LONG == storage
                || BigInteger.ZERO == storage
    }

    @JvmStatic
    fun floorDouble(n: Double): Int {
        val i = n.toInt()
        return if (n >= i) i else i - 1
    }

    @JvmStatic
    fun ceilDouble(n: Double): Int {
        val i = n.toInt()
        return if (n > i) i + 1 else i
    }

    @JvmStatic
    fun floorFloat(n: Float): Int {
        val i = n.toInt()
        return if (n >= i) i else i - 1
    }

    @JvmStatic
    fun ceilFloat(n: Float): Int {
        val i = n.toInt()
        return if (n > i) i + 1 else i
    }

    @JvmOverloads
    fun randomRange(random: NukkitRandom, start: Int = 0): Int {
        return randomRange(random, 0, 0x7fffffff)
    }

    fun randomRange(random: NukkitRandom, start: Int, end: Int): Int {
        return start + (random.nextInt() % (end + 1 - start))
    }

    @JvmStatic
    @JvmOverloads
    fun round(d: Double, precision: Int = 0): Double {
        val pow = 10.0.pow(precision.toDouble())
        return (Math.round(d * pow).toDouble()) / pow
    }

    fun clamp(value: Double, min: Double, max: Double): Double {
        return if (value < min) min else (min(value, max))
    }

    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return if (value < min) min else (min(value.toDouble(), max.toDouble())).toInt()
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else (min(value.toDouble(), max.toDouble())).toFloat()
    }

    fun getDirection(diffX: Double, diffZ: Double): Double {
        var diffX = diffX
        var diffZ = diffZ
        diffX = abs(diffX)
        diffZ = abs(diffZ)

        return max(diffX, diffZ)
    }

    fun bitLength(data: Byte): Int {
        var data = data
        if (data < 0) {
            return 32
        }

        if (data.toInt() == 0) {
            return 1
        }

        var bits = 0
        while (data.toInt() != 0) {
            data = (data.toInt() ushr 1).toByte()
            bits++
        }

        return bits
    }

    fun bitLength(data: Int): Int {
        var data = data
        if (data < 0) {
            return 32
        }

        if (data == 0) {
            return 1
        }

        var bits = 0
        while (data != 0) {
            data = data ushr 1
            bits++
        }

        return bits
    }

    fun bitLength(data: Long): Int {
        var data = data
        if (data < 0) {
            return 64
        }

        if (data == 0L) {
            return 1
        }

        var bits = 0
        while (data != 0L) {
            data = data ushr 1
            bits++
        }

        return bits
    }

    fun bitLength(data: BigInteger): Int {
        if (data.compareTo(BigInteger.ZERO) < 0) {
            throw UnsupportedOperationException("Negative BigIntegers are not supported (nearly infinite bits)")
        }

        return data.bitLength()
    }
}
