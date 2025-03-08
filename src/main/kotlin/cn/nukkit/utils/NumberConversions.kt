package cn.nukkit.utils

/**
 * Utils for casting number types to other number types
 */
object NumberConversions {
    fun floor(num: Double): Int {
        val floor = num.toInt()
        return if (floor.toDouble() == num) floor else floor - (java.lang.Double.doubleToRawLongBits(num) ushr 63).toInt()
    }

    fun ceil(num: Double): Int {
        val floor = num.toInt()
        return if (floor.toDouble() == num) floor else floor + (java.lang.Double.doubleToRawLongBits(num)
            .inv() ushr 63).toInt()
    }

    fun round(num: Double): Int {
        return floor(num + 0.5)
    }

    fun square(num: Double): Double {
        return num * num
    }

    fun toInt(`object`: Any): Int {
        if (`object` is Number) {
            return `object`.toInt()
        }

        try {
            return `object`.toString().toInt()
        } catch (e: NumberFormatException) {
        } catch (e: NullPointerException) {
        }
        return 0
    }

    fun toFloat(`object`: Any): Float {
        if (`object` is Number) {
            return `object`.toFloat()
        }

        try {
            return `object`.toString().toFloat()
        } catch (e: NumberFormatException) {
        } catch (e: NullPointerException) {
        }
        return 0f
    }

    fun toDouble(`object`: Any): Double {
        if (`object` is Number) {
            return `object`.toDouble()
        }

        try {
            return `object`.toString().toDouble()
        } catch (e: NumberFormatException) {
        } catch (e: NullPointerException) {
        }
        return 0.0
    }

    fun toLong(`object`: Any): Long {
        if (`object` is Number) {
            return `object`.toLong()
        }

        try {
            return `object`.toString().toLong()
        } catch (e: NumberFormatException) {
        } catch (e: NullPointerException) {
        }
        return 0
    }

    fun toShort(`object`: Any): Short {
        if (`object` is Number) {
            return `object`.toShort()
        }

        try {
            return `object`.toString().toShort()
        } catch (e: NumberFormatException) {
        } catch (e: NullPointerException) {
        }
        return 0
    }

    fun toByte(`object`: Any): Byte {
        if (`object` is Number) {
            return `object`.toByte()
        }

        try {
            return `object`.toString().toByte()
        } catch (e: NumberFormatException) {
        } catch (e: NullPointerException) {
        }
        return 0
    }
}
